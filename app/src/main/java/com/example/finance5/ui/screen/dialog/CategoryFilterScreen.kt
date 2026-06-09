package com.example.finance5.ui.screen.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.finance5.ui.state.item.CategoryItemUiState
import com.example.finance5.ui.viewmodel.CategoryViewModel
import com.example.finance5.ui.viewmodel.FilterViewModel

@Composable
fun CategoryFilterScreen(filterViewModel: FilterViewModel, categoryViewModel: CategoryViewModel) {
    val categoryUiState by categoryViewModel.uiState.collectAsStateWithLifecycle()
    val filterUiState by filterViewModel.uiState.collectAsStateWithLifecycle()

    categoryViewModel.fetchCategories()
    filterViewModel.updateSelectedCategories()

    val categories = categoryUiState.categoryItemUiStateList
    val selectedCategories = filterUiState.selectedCategories

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp, 30.dp)
            .clip(RoundedCornerShape(10))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp, 0.dp)
        ) {
            Text("Выберите категории")
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
            ) {
                items(categories) { CategoryFilterItem(it, isSelected = it in selectedCategories) }
            }
            TextButton(
                onClick = {}
            ) {
                Text("Применить")
            }
        }
    }
}

@Composable
fun CategoryFilterItem(category: CategoryItemUiState, isSelected: Boolean) {
    val mod = Modifier.fillMaxWidth().height(30.dp)


    Box(
        modifier = if (isSelected) {
            mod.background(Color.Green)
        }
        else {
            mod
        }
    ) {
        Text(category.name)
    }
}