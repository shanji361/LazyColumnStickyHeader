package com.example.lazycolumnstickyheader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lazycolumnstickyheader.ui.theme.LazyColumnStickyHeaderTheme
import kotlinx.coroutines.launch

private const val TAG = "ContactListDemo"

// Data class for Contact
data class Contact(val name: String, val phoneNumber: String)

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LazyColumnStickyHeaderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContactListScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

// Function to generate sample contacts
fun generateContacts(count: Int): List<Contact> {
    val firstNames = listOf(
        "Alice", "Andrew", "Amy", "Aaron", "Amber",
        "Bob", "Brian", "Bella", "Brandon", "Brittany",
        "Charlie", "Chris", "Claire", "Cameron", "Chloe",
        "David", "Daniel", "Diana", "Derek", "Danielle",
        "Ethan", "Emma", "Eric", "Emily", "Evan",
        "Frank", "Fiona", "Fred", "Felicia", "Felix",
        "George", "Grace", "Gary", "Gina", "Grant",
        "Henry", "Hannah", "Harry", "Heather", "Hugo",
        "Isaac", "Isabella", "Ian", "Iris", "Ivan",
        "Jack", "Jessica", "James", "Julia", "Jordan",
        "Kevin", "Kate", "Kyle", "Karen", "Keith",
        "Laura", "Luke", "Lisa", "Leo", "Lily",
        "Michael", "Maria", "Mark", "Michelle", "Matthew",
        "Nathan", "Nicole", "Noah", "Nancy", "Nick",
        "Oliver", "Olivia", "Oscar", "Owen", "Octavia",
        "Peter", "Paula", "Paul", "Pamela", "Patrick",
        "Quinn", "Queenie", "Quentin", "Quincy", "Quest",
        "Robert", "Rachel", "Ryan", "Rebecca", "Richard",
        "Sarah", "Samuel", "Sophia", "Steven", "Stephanie",
        "Thomas", "Tina", "Tyler", "Taylor", "Tracy",
        "Ulysses", "Uma", "Urban", "Unity", "Ursula",
        "Victor", "Victoria", "Vincent", "Vanessa", "Vera",
        "William", "Wendy", "Walter", "Whitney", "Warren",
        "Xavier", "Xena", "Xander", "Xiomara", "Xion",
        "Yasmine", "Yuri", "Yvonne", "Yale", "Yolanda",
        "Zachary", "Zoe", "Zane", "Zelda", "Zack"
    )


    val contacts = mutableListOf<Contact>()
    for (i in 0 until count) {
        val firstName = firstNames[i % firstNames.size]
        val phoneNumber = "(${(200..999).random()}) ${(200..999).random()}-${(1000..9999).random()}"
        contacts.add(Contact(firstName, phoneNumber))
    }

    return contacts.sortedBy { it.name }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListScreen(modifier: Modifier = Modifier) {
    // Generate contacts and group by first letter
    val contactsByLetter = remember {
        generateContacts(50)
            .groupBy { it.name.first().uppercaseChar() }
            .toSortedMap()
    }

    // State for LazyColumn
    val listState = rememberLazyListState()

    // Determine if FAB should be shown (after scrolling past item 10)
    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 10 }
    }

    // Coroutine scope for animated scrolling
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Contacts",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // LazyColumn with sticky headers
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                // Loop through each letter group
                contactsByLetter.forEach { (letter, contacts) ->
                    // Sticky header for each letter
                    stickyHeader(key = letter) {
                        LetterHeader(letter = letter.toString())
                        Log.d(TAG, "Composing sticky header: $letter")
                    }

                    // Items for contacts under this letter
                    items(
                        items = contacts,
                        key = { contact -> contact.name }
                    ) { contact ->
                        ContactItem(contact = contact)
                        Log.d(TAG, "Composing contact item: ${contact.name}")
                    }
                }
            }
        }

        // FAB, when scrolled past item 10
        if (showScrollToTop) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
    }
}

/**
 * Composable function for the letter sticky header.
 */
@Composable
fun LetterHeader(letter: String) {
    Text(
        text = letter,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

/**
 * Composable function for displaying a single contact item.
 */
@Composable
fun ContactItem(contact: Contact) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = contact.name,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = contact.phoneNumber,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListScreenPreview() {
    LazyColumnStickyHeaderTheme {
        ContactListScreen(Modifier.fillMaxSize())
    }
}