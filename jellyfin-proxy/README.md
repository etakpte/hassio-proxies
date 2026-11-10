# Home Assistant App: Jellyfin Proxy


This app acts as a proxy to an external running Jellyfin instance.
The sole purpose of this app is to add a Jellyfin icon to the sidebar of Home Assistant which will open the frontend of an external running Jellyfin instance.

## Options

- `server` (required): this should be the local URL on which the Jellyfin is running, e.g. `http://192.168.2.43:8096`. Make sure there is no trailing slash!