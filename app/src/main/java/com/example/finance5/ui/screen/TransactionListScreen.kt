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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionListScreen(
    navController: NavController,
    transactionWithCategoryViewModel: TransactionWithCategoryViewModel,
    filterViewModel: FilterViewModel
) {
    val transactionUiState by transactionWithCategoryViewModel.uiState.collectAsStateWithLifecycle()
    val periodUiState by filterViewModel.uiState.collectAsStateWithLifecycle()

    val selectedPeriod = periodUiState.selectedPeriod

    // 1. Фильтруем транзакции по выбранному месяцу и году
    val transactions = transactionUiState.transactionWithCategoryItems
        .filter {
            it.transaction.date?.month == selectedPeriod.month
                    && it.transaction.date?.year == selectedPeriod.year
        }

    // 2. Сортируем транзакции по убыванию даты (от свежих к старым) и группируем
    // Используем LinkedHashMap (автоматически через groupBy), чтобы сохранить порядок ключей
    val groups = transactions
        .sortedByDescending { it.transaction.date }
        .groupBy { it.transaction.date }

    // 3. Создаем форматтер для красивого вывода дня недели на русском
    val russianDayOfWeekFormatter = remember {
        DateTimeFormatter.ofPattern("EEEE", Locale("ru"))
    }

    LazyColumn {
        item {
            CortChart(
                transactions = transactions,
                year = selectedPeriod.year,
                month = selectedPeriod.month
            )
        }

        groups.forEach { (date, dayTransactions) ->
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(10.dp, 0.dp, 10.dp, 0.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Форматируем день недели на русском и делаем первую букву заглавной
                    val dayOfWeekRu = date?.format(russianDayOfWeekFormatter)
                        ?.uppercase() ?: ""

                    Text("$dayOfWeekRu ${date?.dayOfMonth}")
                }
            }
            // Выводим транзакции за этот день
            items(dayTransactions) { transaction ->
                TransactionItem(navController, transaction)
            }
        }
    }
}

@Composable
fun CortChart(
    transactions: List<TransactionWithCategoryItemUiState>,
    year: Int,
    month: java.time.Month
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    // 1. Вычисляем количество дней в выбранном месяце
    val daysInMonth = remember(year, month) {
        java.time.YearMonth.of(year, month).lengthOfMonth()
    }

    // 2. Генерируем данные для КАЖДОГО дня месяца
    val dailyExpenses = remember(transactions, daysInMonth) {
        // Сначала группируем существующие расходы
        val expensesMap = transactions
            .filter { it.category?.type == CategoryType.EXPENSE }
            .groupBy { it.transaction.date?.dayOfMonth ?: 0 }
            .mapValues { (_, items) ->
                items.sumOf { it.transaction.amount ?: 0.0 }.toFloat()
            }

        // Заполняем сетку от 1 до последнего дня месяца
        (1..daysInMonth).map { day ->
            day to (expensesMap[day] ?: 0f) // Если трат не было, ставим 0f
        }
    }

    val chartValues = dailyExpenses.map { it.second }
    val labels = dailyExpenses.map { "${it.first}" }

    LaunchedEffect(dailyExpenses) {
        modelProducer.runTransaction {
            columnSeries { series(chartValues) }
        }
    }

    val bottomAxisFormatter = CartesianValueFormatter { _, x, _ ->
        labels.getOrNull(x.toInt()) ?: ""
    }

    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            // ВЕРТИКАЛЬНАЯ ОСЬ УБРАНА (удалили startAxis)
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = null,
                valueFormatter = bottomAxisFormatter
            ),
        ),
        modelProducer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
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


//
//@Composable
//fun TransactionListScreen(
//    navController: NavController,
//    transactionWithCategoryViewModel: TransactionWithCategoryViewModel,
//    filterViewModel: FilterViewModel
//) {
//    val transactionUiState by transactionWithCategoryViewModel.uiState.collectAsStateWithLifecycle()
//    val periodUiState by filterViewModel.uiState.collectAsStateWithLifecycle()
//
//    val vv = periodUiState.selectedPeriod
//
////    val transactions = transactionUiState.transactionWithCategoryItems
//    val transactions = transactionUiState.transactionWithCategoryItems
//        .filter {
//            it.transaction.date?.month!! == periodUiState.selectedPeriod.month
//                    && it.transaction.date.year == periodUiState.selectedPeriod.year
//        }
//        //.sortedBy { it.transaction.date?.dayOfMonth }
//
//    val groups = transactions.reversed().groupBy { it.transaction.date }
//
//
//    LazyColumn {
//        item { CortChart() }
//        groups.forEach { (date, transactions) ->
//            stickyHeader {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(40.dp)
//                        .padding(10.dp, 0.dp, 10.dp, 0.dp)
//                        .background(Color.White),
//                    contentAlignment = Alignment.CenterStart
//                ) {
//                    Text("${date?.dayOfWeek} ${date?.dayOfMonth}")
//                }
//            }
//            items(transactions) {
//                TransactionItem(navController, it)
//            }
//        }
//    }
//}
//
//@Composable
//fun CortChart() {
//    val modelProducer = remember { CartesianChartModelProducer() }
//    LaunchedEffect(Unit) {
//        modelProducer.runTransaction {
//            columnSeries { series(5, 6, 5, 2, 11, 8, 5, 2, 15, 11, 8, 13, 12, 10, 2, 7) }
//        }
//    }
//
//    val labels = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс", "Пн2", "Вт2", "Ср2", "Чт2", "Пт2", "Сб2", "Вс2", "Пн3", "Вт3")
//
//
//    // 1. Создаем форматтер, который привязывает индекс колонки к тексту
//    val bottomAxisFormatter = CartesianValueFormatter { context, x, _ ->
//        // x — это индекс колонки (0, 1, 2...). Приводим к Int и берем элемент из списка safely
//        labels.getOrNull(x.toInt()) ?: ""
//    }
//
//    CartesianChartHost(
//        rememberCartesianChart(
//            rememberColumnCartesianLayer(),
//            startAxis = VerticalAxis.rememberStart(
//                guideline = null
//            ),
//            // 2. Передаем созданный форматтер в нижнюю ось
//            bottomAxis = HorizontalAxis.rememberBottom(
//                guideline = null,
//                valueFormatter = bottomAxisFormatter
//            ),
//        ),
//        modelProducer,
//    )
//}
//

//
