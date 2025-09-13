package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.models.BlogDTO
import com.breastcancer.breastcancercare.models.BlogCategoryDTO
import com.breastcancer.breastcancercare.models.StateTickWrapper
import com.breastcancer.breastcancercare.models.getTick
import com.breastcancer.breastcancercare.repo.BlogRepository
import com.breastcancer.breastcancercare.states.BlogUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlogViewModel(val blogRepository: BlogRepository) : ViewModel() {
    private var _blogUIDetailState = MutableStateFlow<BlogUIState<BlogDTO>>(BlogUIState.Initial())
    val blogUIDetailState = _blogUIDetailState.asStateFlow()

    private var _blogUIListState =
        MutableStateFlow<BlogUIState<List<BlogDTO>>>(BlogUIState.Initial())
    val blogUIListState = _blogUIListState.asStateFlow()

    private var _allCategories = MutableStateFlow<List<BlogCategoryDTO>>(emptyList())
    val allCategories = _allCategories.asStateFlow()

    private var _selectedCategory = MutableStateFlow<StateTickWrapper<BlogCategoryDTO?>>(
        StateTickWrapper(data = null)
    )
    val selectedCategory = _selectedCategory.asStateFlow()

    init {
        getAllCategories()
        getAllBlogsAndFilterByCategory()
    }

    suspend fun getBlogBySlug(slug: String) {
        _blogUIDetailState.update { _ -> BlogUIState.Loading() }// reset state
        blogRepository.getBlogById(slug = slug).let {
            _blogUIDetailState.update { _ -> BlogUIState.Success(data = it) }
        }
    }

    fun selectCategory(category: BlogCategoryDTO?) =
        _selectedCategory.update { StateTickWrapper(data = category, tick = getTick()) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getAllBlogsAndFilterByCategory() = viewModelScope.launch {
        selectedCategory
            .flatMapLatest { state ->
                _blogUIListState.update { _ -> BlogUIState.Loading() }
                delay(1000L)
                if (state.data == null) blogRepository.getAllBlogs() else blogRepository.getBlogByCategories(
                    categories = listOf(state.data)
                )
            }.collectLatest { blogs ->
                _blogUIListState.update { _ -> BlogUIState.Success(data = blogs) }
            }
    }

    private fun getAllCategories() = viewModelScope.launch(Dispatchers.IO) {
        blogRepository.getAllCategories().collect { categories ->
            _allCategories.update { categories }
        }
    }
}