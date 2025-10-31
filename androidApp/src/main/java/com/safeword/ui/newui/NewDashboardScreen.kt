package com.safeword.ui.newui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.safeword.ui.design.NewUiColors
import com.safeword.ui.design.NewUiDimensions
import com.safeword.ui.design.NewUiTheme
import com.safeword.ui.design.NewUiTypography

@Composable
fun NewDashboardScreen(modifier: Modifier = Modifier) {
    val contacts = remember {
        listOf(
            ContactStub("Fauget Cafe", "1234567890"),
            ContactStub("Larana, Inc.", "1234567890"),
            ContactStub("Claudia Alves", "1234567890"),
            ContactStub("Borcelle Cafe", "1234567890"),
            ContactStub("Avery Clinic", "1234567890")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NewUiColors.background)
            .padding(horizontal = NewUiDimensions.screenPadding, vertical = 32.dp)
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(28.dp))
        QuickActionsRow()
        Spacer(modifier = Modifier.height(NewUiDimensions.sectionSpacing))
        SearchSection()
        Spacer(modifier = Modifier.height(NewUiDimensions.sectionSpacing))
        Text(
            text = "SafeWord Contacts",
            style = NewUiTypography.headingLarge,
            color = NewUiColors.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(contacts) { contact ->
                ContactRow(contact)
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Welcome back",
                style = NewUiTypography.bodyMedium,
                color = NewUiColors.onSurfaceMuted
            )
            Text(
                text = "Alfredo Torres",
                style = NewUiTypography.headingXL,
                color = NewUiColors.onBackground
            )
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(NewUiColors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AT",
                style = NewUiTypography.bodyLarge,
                color = NewUiColors.onBackground
            )
        }
    }
}

@Composable
private fun QuickActionsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuickActionButton(
            label = "Emergency",
            color = NewUiColors.accentRed,
            icon = Icons.Default.Notifications
        )
        QuickActionButton(
            label = "Location",
            color = NewUiColors.primaryVariant,
            icon = Icons.Default.Send
        )
        QuickActionButton(
            label = "Alerts",
            color = NewUiColors.warning,
            icon = Icons.Default.Notifications
        )
        QuickActionButton(
            label = "Settings",
            color = NewUiColors.surfaceVariant,
            icon = Icons.Default.MoreHoriz
        )
        QuickActionButton(
            label = "Pro",
            color = NewUiColors.accentGold,
            icon = Icons.Default.MoreHoriz
        )
    }
}

@Composable
private fun QuickActionButton(
    label: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                tint = Color.White,
                contentDescription = label
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = NewUiTypography.label,
            color = NewUiColors.onSurfaceMuted
        )
    }
}

@Composable
private fun SearchSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NewUiDimensions.cardRadius),
        colors = CardDefaults.cardColors(containerColor = NewUiColors.surfaceElevated)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = NewUiColors.onSurfaceMuted
                    )
                },
                placeholder = {
                    Text("Search here", color = NewUiColors.onSurfaceMuted)
                },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sort by Name",
                    style = NewUiTypography.bodyMedium,
                    color = NewUiColors.onSurfaceMuted
                )
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Sort options",
                        tint = NewUiColors.onSurfaceMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactRow(contact: ContactStub) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NewUiColors.surfaceElevated)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(NewUiColors.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = contact.initials,
                        style = NewUiTypography.bodyLarge,
                        color = NewUiColors.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = contact.name,
                        style = NewUiTypography.bodyLarge,
                        color = NewUiColors.onBackground
                    )
                    Text(
                        text = contact.phone,
                        style = NewUiTypography.bodyMedium,
                        color = NewUiColors.onSurfaceMuted
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconButtonBadge(
                    icon = Icons.Default.Call,
                    tint = NewUiColors.primary
                )
                IconButtonBadge(
                    icon = Icons.Default.Send,
                    tint = NewUiColors.primary
                )
            }
        }
    }
}

@Composable
private fun IconButtonBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(NewUiColors.surfaceVariant)
            .border(
                width = 1.dp,
                color = tint.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint)
    }
}

private data class ContactStub(val name: String, val phone: String) {
    val initials: String =
        name.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("").take(2)
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun NewDashboardScreenPreview() {
    NewUiTheme {
        Surface {
            NewDashboardScreen()
        }
    }
}
