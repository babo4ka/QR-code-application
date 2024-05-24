package com.example.qrcodeapp.widget.creator

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class QrWidgetReceiver: GlanceAppWidgetReceiver()  {
    override val glanceAppWidget: GlanceAppWidget = QrWidget()
}