import React, {PureComponent} from 'react';

export default class PopularFeed extends PureComponent {
    render() {
        return (
            <div className="popular-feed">
                <div className="message--item message--item__single--content">
                    <div className="message--body--wrapper message">
                        <div className="message--body--text">
                            {"BEST TWEET EVER "}<i className="fa fa-heart-o" />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}