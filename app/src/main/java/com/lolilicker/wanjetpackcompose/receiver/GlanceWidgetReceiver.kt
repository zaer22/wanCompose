package com.lolilicker.wanjetpackcompose.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.lolilicker.wanjetpackcompose.storage.sharedpreferences.Pref
import com.lolilicker.wanjetpackcompose.storage.sharedpreferences.Pref.dataStore
import com.lolilicker.wanjetpackcompose.utils.DateUtils
import com.lolilicker.wanjetpackcompose.widget.GlanceWidget
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.util.*

class GlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = GlanceWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        CoroutineScope(Dispatchers.Default).launch {
            updateAppWidget(context)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        CoroutineScope(Dispatchers.Default).launch {
            updateAppWidget(context)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        CoroutineScope(Dispatchers.Default).launch {
            context.dataStore.edit {
                it[booleanPreferencesKey(Pref.APP_WIDGET_ADDED)] = true
            }
            updateAppWidget(context)
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        CoroutineScope(Dispatchers.Default).launch {
            context.dataStore.edit {
                it[booleanPreferencesKey(Pref.APP_WIDGET_ADDED)] = false
            }
        }
    }

    private suspend fun updateAppWidget(context: Context) {
        Log.d("glance", "update App Widget")
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(GlanceWidget::class.java)

        Pref.readLatestPeriodData(context).collect { date ->
            glanceIds.forEach { glanceId ->
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) {
                    it.toMutablePreferences()
                        .apply {
                            this[longPreferencesKey(Pref.LATEST_PERIOD_DATE)] = date
                        }
                }
            }

            glanceAppWidget.updateAll(context = context)
        }
    }
}