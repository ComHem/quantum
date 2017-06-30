import React, {Component} from 'react';
import Posts from './posts/PostsContainer';
import MapBackground from './map/MapContainer';
import PopularFeed from './posts/PopularFeed';

export class App extends Component {
    render() {
        return (
            <div className="main">
                <div className="left--feed">
                    <Posts />
                </div>
                <div className="right--feed">
                    <MapBackground />
                </div>
                {/*<footer>*/}
                    {/*<PopularFeed />*/}
                {/*</footer>*/}
            </div>
        );
    }
}
