package com.safeword.ui.main

import android.view.LayoutInflater
import androidx.annotation.StringRes
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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.NativeAd
import com.safeword.R
import com.safeword.databinding.ViewNativeAdBinding
import com.safeword.shared.bridge.model.PeerBridgeState

enum class MainDestination {
    Dashboard,
    Contacts,
    SafeWords,
    Settings
}

private data class NavigationItem(
    val destination: MainDestination,
    @StringRes val labelRes: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
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
    onNavigate: (MainDestination) -> Boolean,
    onBindNativeAdView: (NativeAd, ViewNativeAdBinding) -> Unit,
    modifier: Modifier = Modifier
) {
    val destinations = remember {
        listOf(
            NavigationItem(MainDestination.Dashboard, R.string.app_name, Icons.Outlined.Home),
            NavigationItem(MainDestination.Contacts, R.string.contacts, Icons.Outlined.People),
            NavigationItem(MainDestination.SafeWords, R.string.main_action_safe_words_title, Icons.Outlined.Key),
            NavigationItem(MainDestination.Settings, R.string.settings, Icons.Outlined.Settings)
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
                    state = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    onToggleListening = onToggleListening,
                    onNavigate = handleSelection,
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
                    state = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    onToggleListening = onToggleListening,
                    onNavigate = handleSelection,
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
                    state = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    onToggleListening = onToggleListening,
                    onNavigate = handleSelection,
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
                            androidx.compose.material3.Icon(
                                imageVector = item.icon,
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
                            androidx.compose.material3.Icon(
                                imageVector = item.icon,
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
                            androidx.compose.material3.Icon(
                                imageVector = item.icon,
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
    state: MainUiState,
    modifier: Modifier = Modifier,
    onToggleListening: () -> Unit,
    onNavigate: (MainDestination) -> Unit,
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
            DashboardHeader()
            StatusChip(listening = state.listeningEnabled)
            ListeningControl(state.listeningEnabled, onToggleListening)
            QuickActionsRow(onNavigate = onNavigate)
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
private fun DashboardHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.ic_logo_safeword),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineSmall.copy(color = colorResource(id = R.color.safeword_on_surface))
            )
            Text(
                text = stringResource(id = R.string.main_headline),
                style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.safeword_on_surface_variant))
            )
        }
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
                .size(260.dp)
                .background(gradient, CircleShape)
                .clickable { onToggleListening() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.ic_mic_primary),
                    contentDescription = null,
                    tint = if (listening) Color.White else colorResource(id = R.color.accent_teal),
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (listening) {
                        stringResource(id = R.string.main_listening_active)
                    } else {
                        stringResource(id = R.string.main_listening_inactive)
                    },
                    color = colorResource(id = R.color.safeword_on_surface),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun QuickActionsRow(onNavigate: (MainDestination) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionCard(
                title = stringResource(id = R.string.main_action_safe_words_title),
                subtitle = stringResource(id = R.string.main_action_safe_words_subtitle),
                iconRes = R.drawable.ic_logo_safeword,
                iconTint = colorResource(id = R.color.safeword_primary),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainDestination.SafeWords) }
            )
            QuickActionCard(
                title = stringResource(id = R.string.main_action_contacts_title),
                subtitle = stringResource(id = R.string.main_action_contacts_subtitle),
                iconRes = R.drawable.ic_contacts,
                iconTint = colorResource(id = R.color.accent_teal),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainDestination.Contacts) }
            )
        }

        QuickActionCard(
            title = stringResource(id = R.string.settings),
            subtitle = stringResource(id = R.string.main_action_settings_subtitle),
            iconRes = R.drawable.ic_settings,
            iconTint = colorResource(id = R.color.safeword_primary),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigate(MainDestination.Settings) }
        )
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = colorResource(id = R.color.card_background),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    color = colorResource(id = R.color.safeword_on_surface),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = colorResource(id = R.color.safeword_on_surface_variant),
                    style = MaterialTheme.typography.bodySmall
                )
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
            androidx.compose.material3.IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(id = R.string.close_ad),
                    tint = colorResource(id = R.color.safeword_on_surface_variant)
                )
            }
        }
    }
}
