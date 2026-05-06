package com.example.rumedcalendar.ui.screens.upload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rumedcalendar.ui.components.PrimaryActionButton
import com.example.rumedcalendar.ui.theme.MedCalTheme

data class ExtractedUploadItemUi(
    val id: String,
    val title: String,
    val details: String
)

@Composable
fun UploadScreen(
    extractedItems: List<ExtractedUploadItemUi>,
    onPickFileClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onConfirmItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Upload Medical Document",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Choose a source to extract medications and doctor recommendations.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PrimaryActionButton(
                        text = "Pick File",
                        onClick = onPickFileClick
                    )
                    OutlinedButton(
                        onClick = onTakePhotoClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(text = "Take Photo")
                    }
                }
            }

            item {
                Text(
                    text = "Extracted Data",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            if (extractedItems.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "No extracted data yet. Upload a document to continue.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                items(extractedItems, key = { it.id }) { item ->
                    ExtractedItemCard(
                        item = item,
                        onConfirmClick = { onConfirmItemClick(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExtractedItemCard(
    item: ExtractedUploadItemUi,
    onConfirmClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = item.details,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                PrimaryActionButton(
                    text = "Confirm",
                    onClick = onConfirmClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UploadScreenPreview() {
    MedCalTheme {
        UploadScreen(
            extractedItems = listOf(
                ExtractedUploadItemUi(
                    id = "1",
                    title = "Amoxicillin 500 mg",
                    details = "Take 1 tablet twice daily after meals for 7 days."
                ),
                ExtractedUploadItemUi(
                    id = "2",
                    title = "Follow-up Visit",
                    details = "Visit cardiologist next Monday at 10:30 AM."
                )
            ),
            onPickFileClick = {},
            onTakePhotoClick = {},
            onConfirmItemClick = {}
        )
    }
}
