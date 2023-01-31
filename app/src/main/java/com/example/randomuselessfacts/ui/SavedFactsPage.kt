package com.example.randomuselessfacts.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.randomuselessfacts.model.Fact

@Composable
fun SavedFactsPage(viewModel: MainViewModel) {


    val list = viewModel.savedFacts.collectAsState()

    if (list.value.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Nothing here...")
        }
    } else {
        FactsList(list = list.value) { fact ->
            viewModel.saveOrDeleteFact(fact)
        }
    }

}

@Composable
fun FactsList(
    list: List<Fact>,
    delete: (Fact) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy((-8).dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = list,
            key = { item: Fact -> item.id }
        ) { item ->
            FactCard(fact = item, title = null, Icons.Default.Delete) { delete(item) }
        }
    }
}