import React, {PureComponent} from 'react';
import '../../style/map.scss';
import {Map, Marker, TileLayer} from 'react-leaflet';
import {divIcon} from 'leaflet';

export class MapBackground extends PureComponent {
    getGeoCoordinates(post) {
        if (!_.isEmpty(post.location)) {
            return post.location;
        } else if (!_.isEmpty(this.props.map.locations[post.id])) {
            return this.props.map.locations[post.id];
        }
    }

    getLocationFromPlaceIfAvailable(post, nextProps) {
        if (_.isEmpty(post.location) && !_.isEmpty(post.place) && _.isEmpty(nextProps.map.locations[post.id])) {
            this.props.fetchCityLocation(post);
        }
    }

    componentWillUpdate(nextProps) {
        if (this.props.feed.posts !== nextProps.feed.posts) {
            nextProps.feed.posts.forEach((post) => {
                this.getLocationFromPlaceIfAvailable(post, nextProps);
            });
        }
    }

    renderMarker({position, key, icon}) {
        return (
            <Marker key={key}
                    position={position}
                    draggable={false}
                    icon={icon}/>
        );
    }

    render() {
        const parentMarkerIcon = divIcon({
            className: 'map-marker-icon',
            iconAnchor: [12.5, 41],
            iconSize: [25, 41]
        });
        const defaultMapPosition = {
            lat: 64.0,
            lng: 15.0
        };

        return (
            <Map center={defaultMapPosition}
                 zoom={4.5}
                 animate={true}
                 zoomControl={false}
                 scrollWheelZoom={false}>

                <TileLayer url='http://{s}.basemaps.cartocdn.com/dark_nolabels/{z}/{x}/{y}.png'/>

                {this.props.feed.posts && this.props.feed.posts.map((post, i) => {
                    let position = this.getGeoCoordinates(post);

                    return position ? this.renderMarker({
                        key: i,
                        position: position,
                        icon: parentMarkerIcon
                    }) : null
                })}
            </Map>
        );
    }
}
