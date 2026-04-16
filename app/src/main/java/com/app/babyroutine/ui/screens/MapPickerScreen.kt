package com.app.babyroutine.ui.screens

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.app.babyroutine.model.RoutineLocation
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ClickableViewAccessibility")
@Composable
fun MapPickerScreen(
    initialLocation: RoutineLocation? = null,
    onBack: () -> Unit,
    onConfirmLocation: (RoutineLocation) -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    val defaultLatitude = initialLocation?.latitude ?: 48.4284
    val defaultLongitude = initialLocation?.longitude ?: -71.0686

    var selectedLatitude by remember { mutableDoubleStateOf(defaultLatitude) }
    var selectedLongitude by remember { mutableDoubleStateOf(defaultLongitude) }
    var selectedRadius by remember { mutableFloatStateOf(initialLocation?.radius ?: 150f) }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            colors.background,
            colors.surface,
            colors.surfaceVariant.copy(alpha = 0.22f),
            colors.background
        )
    )

    val mapView = remember {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osm_prefs", 0)
        )

        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(15.0)
            controller.setCenter(GeoPoint(defaultLatitude, defaultLongitude))
        }
    }

    fun refreshMap() {
        val point = GeoPoint(selectedLatitude, selectedLongitude)

        mapView.overlays.clear()

        val circle = Polygon().apply {
            points = Polygon.pointsAsCircle(point, selectedRadius.toDouble())
            fillColor = android.graphics.Color.argb(60, 34, 211, 238)
            strokeColor = android.graphics.Color.rgb(34, 211, 238)
            strokeWidth = 4f
        }

        val marker = Marker(mapView).apply {
            position = point
            title = "Lieu sélectionné"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }

        mapView.overlays.add(circle)
        mapView.overlays.add(marker)
        mapView.controller.animateTo(point)
        mapView.invalidate()
    }

    DisposableEffect(selectedLatitude, selectedLongitude, selectedRadius) {
        refreshMap()
        onDispose { }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView.onDetach()
        }
    }

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Associer un lieu",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.onSurface,
                    navigationIconContentColor = colors.onSurface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
                .navigationBarsPadding()
        ) {
            AndroidView(
                factory = {
                    mapView.apply {
                        setOnTouchListener { _, event ->
                            if (event.action == MotionEvent.ACTION_UP) {
                                val geoPoint = mapView.projection.fromPixels(
                                    event.x.toInt(),
                                    event.y.toInt()
                                ) as GeoPoint

                                selectedLatitude = geoPoint.latitude
                                selectedLongitude = geoPoint.longitude
                            }
                            false
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = {
                    refreshMap()
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color(0xFFF2C9F2)
                )

                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = colors.surface.copy(alpha = 0.96f),
                    shadowElevation = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = colors.outline.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(26.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = colors.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Choisir un lieu",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface
                            )
                        }

                        Text(
                            text = "Touchez la carte pour sélectionner la zone de déclenchement.",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(18.dp),
                                color = colors.surfaceVariant.copy(alpha = 0.35f)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Latitude",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.onSurfaceVariant
                                    )
                                    Text(
                                        text = "%.4f".format(selectedLatitude),
                                        fontWeight = FontWeight.SemiBold,
                                        color = colors.onSurface
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(18.dp),
                                color = colors.surfaceVariant.copy(alpha = 0.35f)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Longitude",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.onSurfaceVariant
                                    )
                                    Text(
                                        text = "%.4f".format(selectedLongitude),
                                        fontWeight = FontWeight.SemiBold,
                                        color = colors.onSurface
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Rayon de déclenchement : ${selectedRadius.toInt()} m",
                            color = colors.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )

                        Slider(
                            value = selectedRadius,
                            onValueChange = { selectedRadius = it },
                            valueRange = 50f..500f,
                            colors = SliderDefaults.colors(
                                thumbColor = colors.primary,
                                activeTrackColor = colors.primary.copy(alpha = 0.78f),
                                inactiveTrackColor = colors.outline.copy(alpha = 0.30f)
                            )
                        )

                        Button(
                            onClick = {
                                onConfirmLocation(
                                    RoutineLocation(
                                        latitude = selectedLatitude,
                                        longitude = selectedLongitude,
                                        radius = selectedRadius,
                                        locationName = "Zone personnalisée"
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primary,
                                contentColor = colors.onPrimary
                            )
                        ) {
                            Text(
                                text = "Confirmer",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}