package com.loginworks.royaldines.models;


public class NavDrawerItem {
    private String title;
    private int selectedIcon;
    private int unselectedIcon;
    private String count = "0";
    private boolean isCounterVisible = false;

    public NavDrawerItem() {
    }

    public NavDrawerItem(String title, int icon, int unselectedIcon) {
        this.title = title;
        this.selectedIcon = icon;
        this.unselectedIcon = unselectedIcon;
    }

    public NavDrawerItem(String title) {
        this.title = title;
    }

    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count) {
        this.title = title;
        this.selectedIcon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public int getUnselectedIcon() {
        return unselectedIcon;
    }

    public void setUnselectedIcon(int unselectedIcon) {
        this.unselectedIcon = unselectedIcon;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isCounterVisible() {
        return isCounterVisible;
    }

    public void setIsCounterVisible(boolean isCounterVisible) {
        this.isCounterVisible = isCounterVisible;
    }

    public boolean getCounterVisibility() {
        return this.isCounterVisible;
    }

}
