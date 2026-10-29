# Home Assistant App: qBittorrent Proxy


This app acts as a proxy to an external running qBittorrent instance.
The sole purpose of this app is to add a qBittorrent icon to the sidebar of Home Assistant which will open the frontend of an external running qBittorrent instance.

## Options

- `server` (required): this should be the local URL on which the qBittorrent is running, e.g. `http://192.168.2.43:8080`. Make sure there is no trailing slash!