package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.models.BlogDTO
import com.breastcancer.breastcancercare.repo.BlogRepository
import com.breastcancer.breastcancercare.states.BlogUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlogViewModel(val blogRepository: BlogRepository) : ViewModel() {
    private var _blogUIState = MutableStateFlow<BlogUIState<BlogDTO>>(BlogUIState.Initial())
    val blogUIState = _blogUIState.asStateFlow()

    suspend fun getBlogBySlug(slug: String) {
        _blogUIState.update { _ -> BlogUIState.Loading() }// reset state
            blogRepository.getBlogById(slug = slug).let {
                _blogUIState.update { _ -> BlogUIState.Success(data = it) }
            }
        }
}