package com.example.cryptox.presentation.portfolio

sealed class PortfolioUiEvent {
    data class ShowSnackBar(val message: String): PortfolioUiEvent()
    data class ShowToast(val message: String): PortfolioUiEvent()
    object ItemAdded: PortfolioUiEvent()
    data class ItemAddedLoading(val isLoading: Boolean = false): PortfolioUiEvent()
    object ItemEdited: PortfolioUiEvent()
    data class ItemEditedLoading(val isLoading: Boolean = false): PortfolioUiEvent()
    object ItemDeleted: PortfolioUiEvent()
}

sealed class PortfolioModalState() {
    object Hidden: PortfolioModalState()
    object AddPortfolioItem: PortfolioModalState()
    object EditPortfolioItem: PortfolioModalState()
}