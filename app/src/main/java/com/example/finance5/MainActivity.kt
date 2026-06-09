package com.example.finance5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.room.Room
import com.example.finance5.data.datasource.CategoryLocalDataSource
import com.example.finance5.data.datasource.TransactionLocalDataSource
import com.example.finance5.data.repository.CategoryRepository
import com.example.finance5.data.repository.TransactionRepository
import com.example.finance5.ui.screen.TransactionListScreen
import com.example.finance5.ui.theme.Finance5Theme
import com.example.finance5.ui.viewmodel.CategoryViewModel
import com.example.finance5.ui.viewmodel.TransactionViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finance5.data.entity.Category
import com.example.finance5.ui.screen.CategoryCreateScreen
import com.example.finance5.ui.screen.CategoryEditScreen
import com.example.finance5.ui.screen.CategoryListScreen
import com.example.finance5.ui.screen.SettingsScreen
import com.example.finance5.ui.screen.TransactionCreateScreen
import com.example.finance5.ui.screen.TransactionEditScreen
import com.example.finance5.ui.screen.dialog.CategoryFilterScreen
import com.example.finance5.ui.screen.dialog.DatePickerScreen
import com.example.finance5.ui.screen.dialog.ErrorScreen
import com.example.finance5.ui.viewmodel.CategoryWithTotalSumViewModel
import com.example.finance5.ui.viewmodel.FilterViewModel
import com.example.finance5.ui.viewmodel.TransactionWithCategoryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as FinanceApplication)
        val transactionRepository = appContainer.transactionRepository
        val categoryRepository = appContainer.categoryRepository

        val categoryViewModel = CategoryViewModel(categoryRepository)
        val transactionViewModel = TransactionViewModel(transactionRepository)
        val categoryWithTotalSumViewModel = CategoryWithTotalSumViewModel(categoryRepository)
        val transactionWithCategoryViewModel = TransactionWithCategoryViewModel(transactionRepository)
        val monthYearViewModel = FilterViewModel(categoryRepository)

//        val autoTransaction = Category(
//            name = "Без категории",
//            id = 0,
//            type = CategoryType.EXPENSE
//        )
//        categoryViewModel.insertCategory(autoTransaction)

        enableEdgeToEdge()
        setContent {
            Finance5Theme {
                Main(
                    categoryViewModel,
                    transactionViewModel,
                    categoryWithTotalSumViewModel,
                    transactionWithCategoryViewModel,
                    monthYearViewModel
                )
            }
        }
    }
}

@Composable
fun Main(
    categoryViewModel: CategoryViewModel,
    transactionViewModel: TransactionViewModel,
    categoryWithTotalSumViewModel: CategoryWithTotalSumViewModel,
    transactionWithCategoryViewModel: TransactionWithCategoryViewModel,
    filterViewModel: FilterViewModel
) {
    // TODO Что это?
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // SnackBar Values
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(navController, categoryWithTotalSumViewModel, filterViewModel, currentRoute)
        },
        bottomBar = {
            BottomBar(navController, currentRoute)
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        floatingActionButton = {
            FloatingCreateButton(navController, currentRoute)
        },
    ) {
        Surface(
            modifier = Modifier
                //.background(Color(255,255,200, 0))
                .padding(it)
        ) {
            NavHost(navController, NavRoutes.TransactionListScreen.route) {
                // Transaction
                composable(NavRoutes.TransactionListScreen.route) {
                    TransactionListScreen(navController, transactionWithCategoryViewModel, filterViewModel)
                }
                composable(NavRoutes.TransactionCreateScreen.route) {
                    TransactionCreateScreen(navController, transactionViewModel, categoryViewModel, snackbarHostState, scope)
                }
                composable(
                    NavRoutes.TransactionEditScreen.route + "/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { navBackStack ->
                    TransactionEditScreen(navController, transactionViewModel, categoryViewModel, snackbarHostState, scope, navBackStack.arguments?.getInt("id"))
                }

                // Category
                composable(NavRoutes.CategoryListScreen.route) {
                    CategoryListScreen(navController, categoryWithTotalSumViewModel, filterViewModel)
                }
                composable(NavRoutes.CategoryCreateScreen.route) {
                    CategoryCreateScreen(navController, categoryViewModel, snackbarHostState, scope)
                }
                composable(
                    NavRoutes.CategoryEditScreen.route + "/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { navBackStack ->
                    CategoryEditScreen(navController, categoryViewModel, snackbarHostState, scope, navBackStack.arguments?.getInt("id"))
                }

                // Other
                composable(NavRoutes.SettingsScreen.route) {
                    SettingsScreen(context)
                }

                // dialog
                dialog(NavRoutes.ErrorScreen.route) {
                    ErrorScreen()
                }
                dialog(NavRoutes.CategoryFilterScreen.route) {
                    CategoryFilterScreen(filterViewModel, categoryViewModel)
                }
                dialog(NavRoutes.DatePickerScreen.route) {
                    DatePickerScreen()
                }
            }
        }
    }
}