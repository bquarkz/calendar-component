/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.addon.calendar.client.ui.schedule;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.WidgetUtil;
import org.vaadin.addon.calendar.client.ui.VCalendar;

import java.util.Date;

/**
 * Internally used class by the Calendar
 *
 * since 7.1
 */
public class DateCellContainer extends FlowPanel
        implements MouseDownHandler, MouseUpHandler {

    private Date date;

    private Widget clickTargetWidget;

    private VCalendar calendar;

    private static int borderWidth = -1;

    public DateCellContainer() {
        setStylePrimaryName("v-calendar-datecell");
    }

    public static int measureBorderWidth(DateCellContainer dc) {
        if (borderWidth == -1) {
            borderWidth = WidgetUtil.measureHorizontalBorder(dc.getElement());
        }
        return borderWidth;
    }

    public void setCalendar(VCalendar calendar) {
        this.calendar = calendar;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public boolean hasEvent(int slotIndex) {
        return hasDateCell(slotIndex)
                && ((WeeklyLongItemsDateCell) getChildren().get(slotIndex))
                        .getItem() != null;
    }

    public boolean hasDateCell(int slotIndex) {
        return (getChildren().size() - 1) >= slotIndex;
    }

    public WeeklyLongItemsDateCell getDateCell(int slotIndex) {
        if (!hasDateCell(slotIndex)) {
            addEmptyEventCells(slotIndex - (getChildren().size() - 1));
        }
        return (WeeklyLongItemsDateCell) getChildren().get(slotIndex);
    }

    public void addEmptyEventCells(int eventCount) {
        for (int i = 0; i < eventCount; i++) {
            addEmptyEventCell();
        }
    }

    public void addEmptyEventCell() {
        WeeklyLongItemsDateCell dateCell = new WeeklyLongItemsDateCell();
        dateCell.addMouseDownHandler(this);
        dateCell.addMouseUpHandler(this);
        add(dateCell);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        clickTargetWidget = (Widget) event.getSource();

        event.stopPropagation();
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        if (event.getSource() == clickTargetWidget
                && clickTargetWidget instanceof WeeklyLongItemsDateCell
                && !calendar.isDisabled()) {
            CalendarItem calendarItem = ((WeeklyLongItemsDateCell) clickTargetWidget)
                    .getItem();
            if (calendar.getItemClickListener() != null) {
                calendar.getItemClickListener().itemClick(calendarItem);
            }
        }
    }
}
