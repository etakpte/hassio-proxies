# Home Assistant App: Flexget Proxy


This app acts as a proxy to an external running Flexget webui instance.
The sole purpose of this app is to add a Flexget icon to the sidebar of Home Assistant which will open the frontend of an external running Flexget instance.

## Options

- `server` (required): this should be the local URL on which the Flexget webui is running, e.g. `http://192.168.2.43:8080`. Make sure there is no trailing slash!