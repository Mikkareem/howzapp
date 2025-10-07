package com.techullurgy.howzapp.feature.chat.presentation.di

import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationListViewModel
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatPresentationModule = module {
    viewModelOf(::ConversationListViewModel)
    viewModelOf(::ConversationViewModel)
}