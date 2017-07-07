import React, {Component} from 'react';
import Posts from './posts/PostsContainer';
import MapBackground from './map/MapContainer';

export class App extends Component {
    setPlatform() {
        if (this.props.feed && this.props.feed.posts && this.props.feed.posts[0]) {
            return this.props.feed.posts[0].platform.toLowerCase();
        }
    }

    render() {
        return (
            <div className={`main`}>
                <div className={`background-overlay-color ${this.setPlatform()}`}/>
                <div className="left--feed">
                    <Posts />
                </div>
                <div className="right--feed">
                    <MapBackground />
                </div>
            </div>
        );
    }
}
