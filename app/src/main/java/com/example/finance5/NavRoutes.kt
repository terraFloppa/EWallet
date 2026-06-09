package com.example.finance5

sealed class NavRoutes(val route: String) {
    object TransactionListScreen   : NavRoutes("transactionListScreen")
    object CategoryListScreen      : NavRoutes("categoryListScreen")
    object  TransactionEditScreen  : NavRoutes("transactionEditScreen")
    object  CategoryEditScreen     : NavRoutes("categoryEditScreen")
    object TransactionCreateScreen : NavRoutes("transactionCreateScreen")
    object CategoryCreateScreen    : NavRoutes("categoryCreateScreen")
    object ErrorScreen             : NavRoutes("errorScreen")
    object CategoryFilterScreen    : NavRoutes("categoryFilterScreen")
    object DatePickerScreen        : NavRoutes("datePickerScreen")
}