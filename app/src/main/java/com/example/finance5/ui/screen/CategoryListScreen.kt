package com.example.finance5.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.finance5.data.entity.Category
import com.example.finance5.ui.state.item.CategoryWithTotalSumItemUiState
import com.example.finance5.ui.viewmodel.CategoryWithTotalSumViewModel
import com.example.finance5.ui.viewmodel.FilterViewModel

@Composable
fun CategoryListScreen(
    navController: NavController,
    categoryWithTotalSumViewModel: CategoryWithTotalSumViewModel,
    filterViewModel: FilterViewModel
) {




    val categoryUiState by categoryWithTotalSumViewModel.uiState.collectAsStateWithLifecycle()

    //val periodUiState by periodViewModel.uiState.collectAsStateWithLifecycle()

    val categories = categoryUiState.categoryWithTotalSumItems
    val filteredCategories = categories.filter {
        it.category.type == categoryUiState.chosenType
    }

    LazyColumn {
        items (filteredCategories) { CategoryItem(navController, it) }
    }
}

@Composable
fun CategoryItem(navController: NavController, entry: CategoryWithTotalSumItemUiState) {
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
            .padding(10.dp, 0.dp)
    ) {
        Text(
            text = entry.category.name + " " + entry.category.id,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        when (entry.category.type) {
            CategoryType.EXPENSE ->
                Text(
                    text = "-${entry.totalSum}",
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = Color.Red
                )
            CategoryType.INCOME ->
                Text(
                    text = "+${entry.totalSum}",
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = Color.Green
                )
            else -> {}
        }
    }


//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(80.dp)
//            .padding(10.dp, 0.dp, 10.dp, 10.dp)
//            .clip(RoundedCornerShape(25))
//            .background(Color.White)
//            .clickable(
//                enabled = true,
//
//            )
//    ) {
//        Text("${entry.totalSum}")
//    }
}