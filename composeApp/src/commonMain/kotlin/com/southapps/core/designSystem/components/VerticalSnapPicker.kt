package com.southapps.core.designSystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.southapps.core.designSystem.tokens.Spacing
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun <T> VerticalSnapPicker(
    title: String,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
) {
    val listState = rememberLazyListState()
    val itemHeightPx = with(LocalDensity.current) { 40.dp.toPx() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val initialIndex = items.indexOf(selectedItem)
        listState.scrollToItem(initialIndex)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .collect {
                val offset = listState.firstVisibleItemScrollOffset
                val index = listState.firstVisibleItemIndex

                val centerIndex = if (offset > itemHeightPx / 2) index + 1 else index

                if (centerIndex in items.indices) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(centerIndex)

                        if (selectedItem != items[centerIndex]) {
                            onItemSelected(items[centerIndex])
                        }
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .height(148.dp)
            .width(80.dp)
            .clipToBounds(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(Spacing.md))

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 40.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items) { index, item ->
                val isSelected = item == selectedItem
                Text(
                    text = item.toString(),
                    fontSize = if (isSelected) 24.sp else 18.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                        alpha = .7f
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}