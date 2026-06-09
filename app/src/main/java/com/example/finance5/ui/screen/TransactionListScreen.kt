package com.example.finance5.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finance5.CategoryType
import com.example.finance5.NavRoutes
import com.example.finance5.ui.state.item.TransactionWithCategoryItemUiState
import com.example.finance5.ui.viewmodel.FilterViewModel
import com.example.finance5.ui.viewmodel.TransactionWithCategoryViewModel

@Composable
fun TransactionListScreen(
    navController: NavController,
    transactionWithCategoryViewModel: TransactionWithCategoryViewModel,
    filterViewModel: FilterViewModel
) {
    val transactionUiState by transactionWithCategoryViewModel.uiState.collectAsStateWithLifecycle()
    val periodUiState by filterViewModel.uiState.collectAsStateWithLifecycle()

    val vv = periodUiState.selectedPeriod

//    viewModel.fetchTransactionsWithCategoriesFromPeriod(
//        LocalDate.of(
//            vv.year,
//            vv.month - 1,
//            vv.dayOfMonth
//        ),
//        LocalDate.of(
//            vv.year,
//            vv.month + 1,
//            vv.dayOfMonth
//        )
//    )
    val transactions = transactionUiState.transactionWithCategoryItems


//    val transactions = uiState.transactionWithCategoryItems.filter {
//        it.transaction.date?.month!! == periodUiState.chosenYearMonth.month
//                && it.transaction.date.year == periodUiState.chosenYearMonth.year
//    }

    val groups = transactions.groupBy { it.transaction.date }

    LazyColumn {
        groups.forEach { (date, transactions) ->
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(10.dp, 0.dp, 10.dp, 0.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text("${date?.dayOfWeek} ${date?.dayOfMonth}")
                }
            }
            items(transactions) {
                TransactionItem(navController, it)
            }
        }
    }
}

@Composable
fun TransactionItem(navController: NavController, entry: TransactionWithCategoryItemUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp, 0.dp, 10.dp, 10.dp)
            .clip(RoundedCornerShape(25))
            .background(Color.White)
            .clickable(
                enabled = true,
                onClick = {
                    navController.navigate(NavRoutes.TransactionEditScreen.route + "/${entry.transaction.id}")
                }
            )
            .padding(10.dp, 0.dp)
    ) {
        when (entry.category?.type) {
            CategoryType.EXPENSE ->
                Text(
                    text = "-${entry.transaction.amount}",
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = Color.Red
                )
            CategoryType.INCOME ->
                Text(
                    text = "+${entry.transaction.amount}",
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = Color.Green
                )
            else -> {}
        }
        Text(
            text = "${entry.category?.name}",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

