package com.safeword.ui.main

import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.NativeAd
import com.safeword.R
import com.safeword.databinding.ViewNativeAdBinding
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.ContactLinkStatus

enum class MainDestination {
    Dashboard,
    Contacts,
    SafeWords,
    Settings
}

enum class ContactSortOption {
    Name,
    Recent
}

private data class NavigationItem(
    val destination: MainDestination,
    @StringRes val labelRes: Int,
    val iconRes: Int
)

@Composable
fun MainScreen(
    windowSizeClass: WindowSizeClass,
    state: MainUiState,
    snackbarHostState: SnackbarHostState,
    nativeAd: NativeAd?,
    adVisible: Boolean,
    adsEnabled: Boolean,
    onCloseAd: () -> Unit,
    onToggleListening: () -> Unit,
    onAddContact: () -> Unit,
    onOpenLocation: () -> Unit,
    onOpenAlerts: () -> Unit,
    onOpenSettingsShortcut: () -> Unit,
    onNavigate: (MainDestination) -> Boolean,
    onUpgradeToPro: () -> Unit,
    onGiftPro: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onContactSelected: (Contact) -> Unit,
    onContactCall: (Contact, Boolean) -> Unit,
    onContactMessage: (Contact) -> Unit,
    onBindNativeAdView: (NativeAd, ViewNativeAdBinding) -> Unit,
    modifier: Modifier = Modifier
) {
    val destinations = remember {
        listOf(
            NavigationItem(MainDestination.Dashboard, R.string.app_name, R.drawable.ic_logo_safeword),
            NavigationItem(MainDestination.Contacts, R.string.contacts, R.drawable.ic_contacts),
            NavigationItem(MainDestination.SafeWords, R.string.main_action_safe_words_title, R.drawable.ic_mic_primary),
            NavigationItem(MainDestination.Settings, R.string.settings, R.drawable.ic_settings)
        )
    }

    var selectedDestination by remember { mutableStateOf(MainDestination.Dashboard) }

    val handleSelection: (MainDestination) -> Unit = { destination ->
        if (destination == MainDestination.Dashboard) {
            selectedDestination = MainDestination.Dashboard
        } else {
            val keepSelection = onNavigate(destination)
            selectedDestination = if (keepSelection) destination else MainDestination.Dashboard
        }
    }

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var sortOption by rememberSaveable { mutableStateOf(ContactSortOption.Name) }
    val filteredContacts = remember(state.contacts, searchQuery, sortOption) {
        val trimmed = searchQuery.trim()
        state.contacts
            .filter { contact ->
                if (trimmed.isEmpty()) {
                    true
                } else {
                    contact.name.contains(trimmed, ignoreCase = true) ||
                        contact.phone.contains(trimmed, ignoreCase = true) ||
                        contact.email?.contains(trimmed, ignoreCase = true) == true
                }
            }
            .let { contacts ->
                when (sortOption) {
                    ContactSortOption.Name -> contacts.sortedBy { it.name.lowercase() }
                    ContactSortOption.Recent -> contacts.sortedByDescending { it.createdAtMillis }
                }
            }
    }

    val planLabel = if (adsEnabled) {
        stringResource(id = R.string.dashboard_plan_badge_free)
    } else {
        stringResource(id = R.string.dashboard_plan_badge_pro)
    }

    val proAction: () -> Unit = {
        if (adsEnabled) onUpgradeToPro() else onGiftPro()
    }

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactNavigationLayout(
                modifier = modifier,
                snackbarHostState = snackbarHostState,
                destinations = destinations,
                selectedDestination = selectedDestination,
                onDestinationSelected = handleSelection
            ) { padding ->
                DashboardContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    state = state,
                    planLabel = planLabel,
                    filteredContacts = filteredContacts,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    sortOption = sortOption,
                    onSortOptionChange = { sortOption = it },
                    onToggleListening = onToggleListening,
                    onAddContact = onAddContact,
                    onOpenLocation = onOpenLocation,
                    onOpenAlerts = onOpenAlerts,
                    onOpenSettings = onOpenSettingsShortcut,
                    onOpenPro = proAction,
                    onNavigateToContacts = onNavigateToContacts,
                    onContactSelected = onContactSelected,
                    onContactCall = onContactCall,
                    onContactMessage = onContactMessage,
                    adsEnabled = adsEnabled,
                    onUpgradeToPro = onUpgradeToPro,
                    onGiftPro = onGiftPro,
                    nativeAd = if (adsEnabled && adVisible) nativeAd else null,
                    onCloseAd = onCloseAd,
                    onBindNativeAdView = onBindNativeAdView
                )
            }
        }

        WindowWidthSizeClass.Medium -> {
            MediumNavigationLayout(
                modifier = modifier,
                snackbarHostState = snackbarHostState,
                destinations = destinations,
                selectedDestination = selectedDestination,
                onDestinationSelected = handleSelection
            ) { padding ->
                DashboardContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    state = state,
                    planLabel = planLabel,
                    filteredContacts = filteredContacts,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    sortOption = sortOption,
                    onSortOptionChange = { sortOption = it },
                    onToggleListening = onToggleListening,
                    onAddContact = onAddContact,
                    onOpenLocation = onOpenLocation,
                    onOpenAlerts = onOpenAlerts,
                    onOpenSettings = onOpenSettingsShortcut,
                    onOpenPro = proAction,
                    onNavigateToContacts = onNavigateToContacts,
                    onContactSelected = onContactSelected,
                    onContactCall = onContactCall,
                    onContactMessage = onContactMessage,
                    adsEnabled = adsEnabled,
                    onUpgradeToPro = onUpgradeToPro,
                    onGiftPro = onGiftPro,
                    nativeAd = if (adsEnabled && adVisible) nativeAd else null,
                    onCloseAd = onCloseAd,
                    onBindNativeAdView = onBindNativeAdView
                )
            }
        }

        else -> {
            ExpandedNavigationLayout(
                modifier = modifier,
                snackbarHostState = snackbarHostState,
                destinations = destinations,
                selectedDestination = selectedDestination,
                onDestinationSelected = handleSelection
            ) { padding ->
                DashboardContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    state = state,
                    planLabel = planLabel,
                    filteredContacts = filteredContacts,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    sortOption = sortOption,
                    onSortOptionChange = { sortOption = it },
                    onToggleListening = onToggleListening,
                    onAddContact = onAddContact,
                    onOpenLocation = onOpenLocation,
                    onOpenAlerts = onOpenAlerts,
                    onOpenSettings = onOpenSettingsShortcut,
                    onOpenPro = proAction,
                    onNavigateToContacts = onNavigateToContacts,
                    onContactSelected = onContactSelected,
                    onContactCall = onContactCall,
                    onContactMessage = onContactMessage,
                    adsEnabled = adsEnabled,
                    onUpgradeToPro = onUpgradeToPro,
                    onGiftPro = onGiftPro,
                    nativeAd = if (adsEnabled && adVisible) nativeAd else null,
                    onCloseAd = onCloseAd,
                    onBindNativeAdView = onBindNativeAdView
                )
            }
        }
    }
}

@Composable
private fun CompactNavigationLayout(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    destinations: List<NavigationItem>,
    selectedDestination: MainDestination,
    onDestinationSelected: (MainDestination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                destinations.forEach { item ->
                    NavigationBarItem(
                        selected = selectedDestination == item.destination,
                        onClick = { onDestinationSelected(item.destination) },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = stringResource(id = item.labelRes)
                            )
                        },
                        label = { Text(text = stringResource(id = item.labelRes)) }
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding -> content(padding) }
}

@Composable
private fun MediumNavigationLayout(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    destinations: List<NavigationItem>,
    selectedDestination: MainDestination,
    onDestinationSelected: (MainDestination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NavigationRail {
                destinations.forEach { item ->
                    NavigationRailItem(
                        selected = selectedDestination == item.destination,
                        onClick = { onDestinationSelected(item.destination) },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = stringResource(id = item.labelRes)
                            )
                        },
                        label = { Text(text = stringResource(id = item.labelRes)) }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                content(PaddingValues())
            }
        }
    }
}

@Composable
private fun ExpandedNavigationLayout(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    destinations: List<NavigationItem>,
    selectedDestination: MainDestination,
    onDestinationSelected: (MainDestination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    PermanentNavigationDrawer(
        modifier = modifier,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(240.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                destinations.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(id = item.labelRes)) },
                        selected = selectedDestination == item.destination,
                        onClick = { onDestinationSelected(item.destination) },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = stringResource(id = item.labelRes)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding -> content(padding) }
    }
}

@Composable
private fun DashboardContent(
    modifier: Modifier,
    state: MainUiState,
    planLabel: String,
    filteredContacts: List<Contact>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    sortOption: ContactSortOption,
    onSortOptionChange: (ContactSortOption) -> Unit,
    onToggleListening: () -> Unit,
    onAddContact: () -> Unit,
    onOpenLocation: () -> Unit,
    onOpenAlerts: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPro: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onContactSelected: (Contact) -> Unit,
    onContactCall: (Contact, Boolean) -> Unit,
    onContactMessage: (Contact) -> Unit,
    adsEnabled: Boolean,
    onUpgradeToPro: () -> Unit,
    onGiftPro: () -> Unit,
    nativeAd: NativeAd?,
    onCloseAd: () -> Unit,
    onBindNativeAdView: (NativeAd, ViewNativeAdBinding) -> Unit
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    colorResource(id = R.color.gradient_primary_start),
                    colorResource(id = R.color.gradient_primary_end)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DashboardHeader(planLabel = planLabel)
            StatusChip(listening = state.listeningEnabled)
            ListeningControl(state.listeningEnabled, onToggleListening)
            QuickActionRow(
                onAddContact = onAddContact,
                onOpenLocation = onOpenLocation,
                onOpenAlerts = onOpenAlerts,
                onOpenSettings = onOpenSettings,
                onOpenPro = onOpenPro
            )

            if (adsEnabled) {
                UpgradeCalloutCard(onUpgradeToPro)
            } else if (state.linkedFreeContacts > 0) {
                GiftCalloutCard(state.linkedFreeContacts, onGiftPro)
            }

            SearchAndSortRow(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                sortOption = sortOption,
                onSortOptionChange = onSortOptionChange
            )

            ContactListSection(
                contacts = filteredContacts,
                onContactSelected = onContactSelected,
                onContactCall = onContactCall,
                onContactMessage = onContactMessage,
                onNavigateToContacts = onNavigateToContacts
            )

            StatusCard(state)

            if (nativeAd != null) {
                NativeAdCard(
                    nativeAd = nativeAd,
                    onClose = onCloseAd,
                    onBindNativeAdView = onBindNativeAdView
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
@Composable
private fun DashboardHeader(planLabel: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_safeword),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.dashboard_welcome_back),
                    style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.safeword_on_surface_variant))
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall.copy(color = colorResource(id = R.color.safeword_on_surface)),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = colorResource(id = R.color.card_background),
            tonalElevation = 2.dp
        ) {
            Text(
                text = planLabel,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelLarge.copy(color = colorResource(id = R.color.accent_amber))
            )
        }
    }
}

@Composable
private fun QuickActionRow(
    onAddContact: () -> Unit,
    onOpenLocation: () -> Unit,
    onOpenAlerts: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPro: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionButton(
            label = stringResource(id = R.string.dashboard_action_add),
            iconRes = R.drawable.ic_dashboard_add,
            background = colorResource(id = R.color.card_background),
            onClick = onAddContact
        )
        QuickActionButton(
            label = stringResource(id = R.string.dashboard_action_location),
            iconRes = R.drawable.ic_dashboard_location,
            background = colorResource(id = R.color.card_background),
            onClick = onOpenLocation
        )
        QuickActionButton(
            label = stringResource(id = R.string.dashboard_action_alerts),
            iconRes = R.drawable.ic_dashboard_alerts,
            background = colorResource(id = R.color.card_background),
            onClick = onOpenAlerts
        )
        QuickActionButton(
            label = stringResource(id = R.string.dashboard_action_settings),
            iconRes = R.drawable.ic_settings,
            background = colorResource(id = R.color.card_background),
            onClick = onOpenSettings
        )
        QuickActionButton(
            label = stringResource(id = R.string.dashboard_action_pro),
            iconRes = R.drawable.ic_dashboard_crown,
            background = colorResource(id = R.color.gradient_alert_start),
            onClick = onOpenPro
        )
    }
}

@Composable
private fun QuickActionButton(
    label: String,
    iconRes: Int,
    background: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(color = colorResource(id = R.color.safeword_on_surface)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchAndSortRow(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    sortOption: ContactSortOption,
    onSortOptionChange: (ContactSortOption) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null
                        )
                    }
                }
            },
            placeholder = { Text(text = stringResource(id = R.string.dashboard_search_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.card_background),
                unfocusedContainerColor = colorResource(id = R.color.card_background),
                focusedTextColor = colorResource(id = R.color.safeword_on_surface),
                unfocusedTextColor = colorResource(id = R.color.safeword_on_surface),
                cursorColor = colorResource(id = R.color.accent_teal)
            )
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SortChip(
                label = stringResource(id = R.string.dashboard_sort_name),
                selected = sortOption == ContactSortOption.Name,
                onClick = { onSortOptionChange(ContactSortOption.Name) }
            )
            SortChip(
                label = stringResource(id = R.string.dashboard_sort_recent),
                selected = sortOption == ContactSortOption.Recent,
                onClick = { onSortOptionChange(ContactSortOption.Recent) }
            )
        }
    }
}

@Composable
private fun SortChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text = label) },
        leadingIcon = if (selected) {
            {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dashboard_sort),
                    contentDescription = null
                )
            }
        } else {
            null
        }
    )
}

@Composable
private fun ContactListSection(
    contacts: List<Contact>,
    onContactSelected: (Contact) -> Unit,
    onContactCall: (Contact, Boolean) -> Unit,
    onContactMessage: (Contact) -> Unit,
    onNavigateToContacts: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(id = R.string.dashboard_contacts_section_title),
            style = MaterialTheme.typography.titleLarge.copy(color = colorResource(id = R.color.safeword_on_surface)),
            fontWeight = FontWeight.SemiBold
        )

        if (contacts.isEmpty()) {
            Text(
                text = stringResource(id = R.string.dashboard_contacts_empty),
                style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.safeword_on_surface_variant))
            )
        } else {
            contacts.take(6).forEach { contact ->
                ContactRow(
                    contact = contact,
                    onSelected = { onContactSelected(contact) },
                    onCall = { emergency -> onContactCall(contact, emergency) },
                    onMessage = { onContactMessage(contact) }
                )
            }

            if (contacts.size > 6) {
                TextButton(onClick = onNavigateToContacts) {
                    Text(text = stringResource(id = R.string.dashboard_view_all_contacts))
                }
            }
        }
    }
}

@Composable
private fun ContactRow(
    contact: Contact,
    onSelected: () -> Unit,
    onCall: (Boolean) -> Unit,
    onMessage: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected),
        shape = RoundedCornerShape(20.dp),
        color = colorResource(id = R.color.card_background)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    color = colorResource(id = R.color.card_background_variant),
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_contacts),
                            contentDescription = null,
                            tint = colorResource(id = R.color.accent_teal),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleMedium.copy(color = colorResource(id = R.color.safeword_on_surface)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = contact.phone,
                        style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.safeword_on_surface_variant)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    contact.email?.takeIf { it.isNotBlank() }?.let { email ->
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodySmall.copy(color = colorResource(id = R.color.safeword_on_surface_variant)),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (contact.linkStatus == ContactLinkStatus.LINKED) {
                    Surface(
                        color = colorResource(id = R.color.accent_teal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.contact_detail_linked_badge),
                            style = MaterialTheme.typography.labelSmall.copy(color = colorResource(id = R.color.safeword_on_secondary)),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ContactActionIcon(
                    background = colorResource(id = R.color.contact_action_call),
                    iconRes = R.drawable.ic_call,
                    contentDescription = stringResource(id = R.string.action_call_contact),
                    onClick = { onCall(false) }
                )
                ContactActionIcon(
                    background = colorResource(id = R.color.contact_action_emergency),
                    iconRes = R.drawable.ic_call,
                    contentDescription = stringResource(id = R.string.emergency_call),
                    onClick = { onCall(true) }
                )
                ContactActionIcon(
                    background = colorResource(id = R.color.contact_action_message),
                    iconRes = R.drawable.ic_message,
                    contentDescription = stringResource(id = R.string.action_text_contact),
                    onClick = onMessage
                )
            }
        }
    }
}

@Composable
private fun ContactActionIcon(
    background: Color,
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun StatusChip(listening: Boolean) {
    Surface(
        shape = RoundedCornerShape(50),
        color = colorResource(id = R.color.status_chip_bg)
    ) {
        Text(
            text = if (listening) {
                stringResource(id = R.string.main_status_listening_chip_on)
            } else {
                stringResource(id = R.string.main_status_listening_chip_off)
            },
            color = colorResource(id = R.color.status_chip_text),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ListeningControl(
    listening: Boolean,
    onToggleListening: () -> Unit
) {
    val gradient = if (listening) {
        Brush.verticalGradient(
            colors = listOf(
                colorResource(id = R.color.gradient_secondary_start),
                colorResource(id = R.color.gradient_secondary_end)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                colorResource(id = R.color.listening_button_inactive),
                colorResource(id = R.color.listening_button_inactive)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
                .background(brush = gradient)
                .clickable(onClick = onToggleListening),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (listening) {
                        stringResource(id = R.string.main_listening_active)
                    } else {
                        stringResource(id = R.string.main_listening_inactive)
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontSize = 20.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (listening) {
                        stringResource(id = R.string.main_listening_subtitle_on)
                    } else {
                        stringResource(id = R.string.main_listening_subtitle_off)
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun UpgradeCalloutCard(onUpgrade: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.card_background))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.main_action_safe_words_title),
                style = MaterialTheme.typography.titleLarge,
                color = colorResource(id = R.color.safeword_on_surface)
            )
            Text(
                text = stringResource(id = R.string.main_action_safe_words_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.safeword_on_surface_variant)
            )
            Button(onClick = onUpgrade) {
                Text(text = stringResource(id = R.string.dashboard_action_pro))
            }
        }
    }
}

@Composable
private fun GiftCalloutCard(
    linkedFreeContacts: Int,
    onGift: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.card_background_variant))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.gift_card_title),
                style = MaterialTheme.typography.titleLarge,
                color = colorResource(id = R.color.safeword_on_surface)
            )
            Text(
                text = pluralStringResource(
                    id = R.plurals.gift_card_subtitle,
                    count = linkedFreeContacts,
                    linkedFreeContacts
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.safeword_on_surface_variant)
            )
            Button(onClick = onGift) {
                Text(text = stringResource(id = R.string.gift_card_cta))
            }
        }
    }
}

@Composable
private fun StatusCard(state: MainUiState) {
    val linkStatusText = when {
        state.safewordContacts <= 0 -> stringResource(id = R.string.link_status_disconnected)
        state.peerState is PeerBridgeState.Connected && state.peerState.peerCount > 0 ->
            pluralStringResource(
                id = R.plurals.link_status_active,
                count = state.safewordContacts,
                state.safewordContacts
            )

        else -> pluralStringResource(
            id = R.plurals.link_status_waiting,
            count = state.safewordContacts,
            state.safewordContacts
        )
    }

    val peerStatus = when (val peerState = state.peerState) {
        is PeerBridgeState.Connected -> stringResource(id = R.string.peer_connected)
        is PeerBridgeState.Error -> peerState.message
        else -> stringResource(id = R.string.peer_disconnected)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.card_background_variant))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = linkStatusText,
                color = colorResource(id = R.color.safeword_on_surface),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(id = R.string.main_status_peer_title),
                color = colorResource(id = R.color.safeword_on_surface_variant),
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = peerStatus,
                color = colorResource(id = R.color.safeword_on_surface),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(id = R.string.main_status_contacts_title),
                color = colorResource(id = R.color.safeword_on_surface_variant),
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = pluralStringResource(
                    id = R.plurals.contacts_count,
                    count = state.totalContacts,
                    state.totalContacts
                ),
                color = colorResource(id = R.color.safeword_on_surface),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun NativeAdCard(
    nativeAd: NativeAd,
    onClose: () -> Unit,
    onBindNativeAdView: (NativeAd, ViewNativeAdBinding) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.card_background))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 48.dp),
                factory = { context ->
                    val binding = ViewNativeAdBinding.inflate(LayoutInflater.from(context))
                    onBindNativeAdView(nativeAd, binding)
                    binding.root
                },
                update = { view ->
                    val binding = ViewNativeAdBinding.bind(view)
                    onBindNativeAdView(nativeAd, binding)
                }
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.close_ad),
                    tint = colorResource(id = R.color.safeword_on_surface_variant)
                )
            }
        }
    }
}
