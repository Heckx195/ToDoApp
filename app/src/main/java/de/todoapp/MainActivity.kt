package de.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.todoapp.ui.theme.ToDoAppTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                MyApp(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    Surface(modifier) {
        ToDoPage()
    }
}

@Preview
@Composable
fun MyAppPreview() {
    ToDoAppTheme {
        MyApp(Modifier.fillMaxSize())
    }
}

@Composable
private fun ToDoPage(
    modifier: Modifier = Modifier,
) {
    var todos by rememberSaveable { mutableStateOf(listOf<String>()) }
    var newTodo by rememberSaveable { mutableStateOf("") }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val filteredTodos = todos.filter { it.contains(searchQuery, ignoreCase = true) }

    Column(modifier = modifier.padding(vertical = 4.dp)) {
        SearchBar(
            searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            items(items = filteredTodos) { name ->
                ToDoEntry(name = name, onDelete = { todos = todos - name })
            }
        }
        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = newTodo,
                onValueChange = { newTodo = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("New ToDo") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newTodo.isNotBlank()) {
                    todos = todos + newTodo
                    newTodo = ""
                }
            }) {
                Text("Add")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ToDoPagePreview() {
    ToDoAppTheme {
        ToDoPage()
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = { Text("Search ToDos") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
private fun ToDoEntry(
    name: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = name)
            }
            ElevatedButton(
                onClick = onDelete
            ) {
                Text("Delete")
            }
        }
    }
}