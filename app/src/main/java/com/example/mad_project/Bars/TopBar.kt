package com.example.movieappmad24.components.Bars

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopAppBar(
    title: String,
    sortingAction: TopAppBarAction? = null,
    additionalActions: List<TopAppBarAction> = emptyList(),
    navController: NavController
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            Row {
                sortingAction?.let {
                    IconButton(onClick = it.onClick) {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.contentDescription
                        )
                    }
                }
                additionalActions.forEach { action ->
                    IconButton(onClick = action.onClick) {
                        Icon(
                            imageVector = action.icon,
                            contentDescription = action.contentDescription
                        )
                    }
                }
            }
        }
    )
}

data class TopAppBarAction(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val contentDescription: String
)
