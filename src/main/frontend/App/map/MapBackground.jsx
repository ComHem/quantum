import React, {Component} from 'react';
import '../../style/map.scss';
import {Map, Marker, TileLayer} from 'react-leaflet';
import {divIcon} from 'leaflet';

export class MapBackground extends Component {
    calculateCoordinates = function (latest) {
        if (!_.isEmpty(latest.location)) {
            return [latest.location[0], latest.location[1]];
        } else {
            return [-5, -5];
        }
    };

    createMarkerList({position, icon}) {

    }

    render() {
        const defaultMapPosition = [this.props.defaultMapPosition.lat, this.props.defaultMapPosition.lng];
        const parentMarkerIcon = divIcon({
            className: 'map-marker-icon',
            iconSize: [25, 41]
        });

        return (
            <Map center={defaultMapPosition}
                 zoom={4.5}
                 animate={true}
                 zoomControl={false}
                 scrollWheelZoom={false}>

                <TileLayer url='http://{s}.basemaps.cartocdn.com/dark_nolabels/{z}/{x}/{y}.png'/>

                {this.props.feed.posts && this.props.feed.posts.map((latest, i) => {
                    return (
                        <span>
                            <Marker key={i}
                                    position={this.calculateCoordinates(latest)}
                                    draggable={false}
                                    icon={parentMarkerIcon}/>

                            {latest.replies && latest.replies.map((reply, j) => {
                                    let commentIcon = divIcon({
                                        className: `map-marker-icon map-marker-icon--${j + 1}`,
                                        iconSize: [25, 41]
                                    });
                                    console.info(`map-marker-icon--${j + 1}`);
                                    return (
                                        <Marker key={j}
                                                position={this.calculateCoordinates(reply)}
                                                draggable={false}
                                                icon={commentIcon}/>
                                    )
                                }
                            )}
                        </span>
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
