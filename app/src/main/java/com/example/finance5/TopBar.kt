package com.example.finance5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finance5.ui.viewmodel.CategoryWithTotalSumViewModel
import com.example.finance5.ui.viewmodel.FilterViewModel

@Composable
fun TopBar(
    navController: NavController,
    categoryWithTotalSumViewModel: CategoryWithTotalSumViewModel,
    filterViewModel: FilterViewModel,
    currentRoute: String?
) {
    if (currentRoute !in
        listOf(NavRoutes.CategoryListScreen.route, NavRoutes.TransactionListScreen.route))
        return

    val filterUiState by filterViewModel.uiState.collectAsStateWithLifecycle()
    val categoryUiState by categoryWithTotalSumViewModel.uiState.collectAsStateWithLifecycle()
    val period = filterUiState.selectedPeriod
    val currentType = categoryUiState.chosenType

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Переключение месяцов
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { filterViewModel.decrease() },
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape
                ) {
                    Icon(painterResource(R.drawable.ic_chevron_left_icon), contentDescription = "Предыдущий месяц")
                }
                Text("${period.year} ${period.month}")
                IconButton(
                    onClick = { filterViewModel.increase() },
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape
                ) {
                    Icon(painterResource(R.drawable.ic_chevron_right_icon), contentDescription = "Следующий месяц")
                }
            }

            // Кнопка фильтрации
            if (currentRoute == NavRoutes.TransactionListScreen.route) {
                IconButton(
                    onClick = { navController.navigate(NavRoutes.CategoryFilterScreen.route) },
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape
                ) {
                    Icon(painterResource(R.drawable.ic_filter_list_icon), contentDescription = "Фильтр")
                }
            }
        }


        if (currentRoute != NavRoutes.CategoryListScreen.route)
            return

        // Перключение типа транзакций
        SwitchTypeRow(categoryWithTotalSumViewModel, currentType)
    }
}

@Composable
fun SwitchTypeRow(
    categoryWithTotalSumViewModel: CategoryWithTotalSumViewModel,
    currentType: CategoryType
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val underlinedButtonModifier = Modifier.weight(10f)

        SelectTypeButton(underlinedButtonModifier, currentType, CategoryType.EXPENSE) {
            categoryWithTotalSumViewModel.setFilter(CategoryType.EXPENSE)
        }
        SelectTypeButton(underlinedButtonModifier, currentType, CategoryType.INCOME) {
            categoryWithTotalSumViewModel.setFilter(CategoryType.INCOME)
        }
    }
}

@Composable
fun SelectTypeButton(
    modifier: Modifier,
    currentType: CategoryType,
    chosenType: CategoryType,
    onClick: () -> Unit
) {
    val colors: ButtonColors = if (chosenType == currentType) {
        ButtonDefaults.textButtonColors(
            containerColor = Color.LightGray,  // background color
            contentColor = Color.White         // text color
        )
    }
    else {
        ButtonDefaults.textButtonColors()
    }

    TextButton(
        onClick = onClick,
        modifier = modifier,
        shape = RectangleShape,
        colors = colors
    ) {
        Text(chosenType.desc)
    }
}