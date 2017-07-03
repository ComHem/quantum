import React, {Component} from 'react';
import '../../style/map.scss';
import {Map, Marker, TileLayer} from 'react-leaflet';
import {divIcon} from 'leaflet';

export class MapBackground extends Component {
    componentDidMount() {
        // this.props.fetchCityLocation("Stockholm"); //TODO : Location of tweets.
    }

    calculateCordinates = function (latest) {
        if (!_.isEmpty(latest.location)) {
            return [latest.location[0], latest.location[1]];
        } else {
            return [0, 0];
        }
    };

    render() {
        const icon = divIcon({
            className: 'map-marker-icon',
            iconSize: [25, 41]
        });


            const defaultMapPosition = [this.props.defaultMapPosition.lat, this.props.defaultMapPosition.lng];
            return (
                <Map center={defaultMapPosition}
                     zoom={4.2}
                     animate={true}
                     zoomControl={false}
                     scrollWheelZoom={false}>

                    <TileLayer url='http://{s}.basemaps.cartocdn.com/dark_nolabels/{z}/{x}/{y}.png'/>

                    {this.props.feed.posts && this.props.feed.posts.map((latest, j) => {
                        return (
                            <Marker key={j}
                                    position={this.calculateCordinates(latest)}
                                    draggable={false}
                                    icon={icon}
                                    ref="marker"/>
                        );
                    })}
                </Map>
            );
    }
}

MapBackground.defaultProps = {
    defaultMapPosition: {
        lat: 64.0,
        lng: 15.0
    }
};
