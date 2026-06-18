package com.example.finance5.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finance5.CategoryType
import com.example.finance5.NavRoutes
import com.example.finance5.ui.state.item.CategoryWithTotalSumItemUiState
import com.example.finance5.ui.viewmodel.CategoryWithTotalSumViewModel
import com.example.finance5.ui.viewmodel.FilterViewModel
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.data.PieChartModelProducer
import com.patrykandpatrick.vico.compose.pie.data.pieSeries
import com.patrykandpatrick.vico.compose.pie.rememberPieChart
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.rememberPieChart
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.rememberPieChart

// 1. Палитра цветов для категорий на графике и в списке
val CategoryColors = listOf(
    Color(0xFF9C27B0), // Фиолетовый
    Color(0xFF2196F3), // Синий
    Color(0xFF009688), // Бирюзовый
    Color(0xFFFF9800), // Оранжевый
    Color(0xFFE91E63), // Розовый
    Color(0xFF4CAF50), // Зеленый
    Color(0xFF3F51B5), // Индиго
    Color(0xFFFFEB3B), // Желтый
    Color(0xFF795548)  // Коричневый
)

@Composable
fun CategoryListScreen(
    navController: NavController,
    categoryWithTotalSumViewModel: CategoryWithTotalSumViewModel,
    filterViewModel: FilterViewModel
) {
    val categoryUiState by categoryWithTotalSumViewModel.uiState.collectAsStateWithLifecycle()

    val categories = categoryUiState.categoryWithTotalSumItems
    val filteredCategories = categories.filter {
        it.category.type == categoryUiState.chosenType
    }

    LazyColumn {
        // Передаем отфильтрованные категории в график
        item {
            PieChart(filteredCategories)
        }
        // Используем itemsIndexed, чтобы знать порядковый индекс для цвета
        itemsIndexed(filteredCategories) { index, item ->
            // Получаем цвет по индексу (циклически, если категорий больше, чем цветов в палитре)
            val color = CategoryColors[index % CategoryColors.size]
            CategoryItem(navController, item, color)
        }
    }
}

@Composable
fun PieChart(filteredCategories: List<CategoryWithTotalSumItemUiState>) {
    val modelProducer = remember { PieChartModelProducer() }

    LaunchedEffect(filteredCategories) {
        modelProducer.runTransaction {
            pieSeries { series(filteredCategories.map { it.totalSum }) }
        }
    }

    if (filteredCategories.isNotEmpty()) {
        // Создаем список объектов PieChart.Slice
        val sliceComponents = filteredCategories.mapIndexed { index, _ ->
            val color = CategoryColors[index % CategoryColors.size]

            com.patrykandpatrick.vico.compose.pie.PieChart.Slice(
                // Вызываем фабричную функцию создания Fill, передавая чистый Compose Color
                fill = com.patrykandpatrick.vico.compose.common.Fill(color = color)
            )
        }

        PieChartHost(
            chart = rememberPieChart(
                sliceProvider = com.patrykandpatrick.vico.compose.pie.PieChart.SliceProvider.Companion.series(
                    sliceComponents
                )
            ),
            modelProducer = modelProducer,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(16.dp)
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Нет данных для отображения")
        }
    }
}


@Composable
fun CategoryItem(
    navController: NavController,
    entry: CategoryWithTotalSumItemUiState,
    categoryColor: Color // 4. Принимаем цвет из LazyColumn
) {
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
                    navController.navigate(NavRoutes.CategoryEditScreen.route + "/${entry.category.id}")
                }
            )
            .padding(16.dp, 0.dp)
    ) {
        // 5. Левая часть: цветной маркер (индикатор) и сумма
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Цветной кружок-индикатор категории
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(color = categoryColor, shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            when (entry.category.type) {
                CategoryType.EXPENSE ->
                    Text(
                        text = "-${entry.totalSum}",
                        color = Color.Red
                    )
                CategoryType.INCOME ->
                    Text(
                        text = "+${entry.totalSum}",
                        color = Color.Green
                    )
                else -> {}
            }
        }

        // Правая часть: название категории
        Text(
            text = entry.category.name,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}
