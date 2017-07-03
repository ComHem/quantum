import React, {PureComponent} from 'react';
import _ from 'lodash';
import '../../style/posts.scss';
import '../../style/animate.scss';
import Animation from './Animation'
import Message from './message/Message';

export default class Posts extends PureComponent {
    constructor(props) {
        super(props);
        this.types = [
            {type: "singles", displayCount: 1},
            {type: "threads", displayCount: 1}
        ];

        this.getPlatformIcon = this.getPlatformIcon.bind(this);
    }

    componentDidMount() {
        this.timer = setInterval(this.displayNewPosts, 12000);
        this.props.fetchFeed().then(() => {
            this.displayNewPosts();
        });
    }

    componentWillUnmount() {
        clearInterval(this.timer);
    }

    getPlatformIcon(platform) {
        return <i className={`fa fa-${platform.toLowerCase()}`}/>
    }

    displayNewPosts = () => {
        if (this.types && this.types.length) {
            this.types.forEach(type => this.fetchWhenEmpty(type));
        }

        const randomTypes = _.shuffle(this.types);

        if (this.props[randomTypes[0].type].length >= randomTypes[0].displayCount) {
            this.props.latestFeed(randomTypes[0]);
        } else if (this.props[randomTypes[1].type].length >= randomTypes[1].displayCount) {
            this.props.latestFeed(randomTypes[1]);
        }
    };

    render() {
        return (
            <div className="posts-container">
                <Animation>
                    {this.props.posts && this.props.posts.map((post, i) => (
                            <Message key={Date.now() + i}
                                     post={post}
                                     type={this.props.type}
                                     getPlatformIcon={this.getPlatformIcon}/>
                        )
                    )}
                </Animation>
            </div>
        );
    }

    fetchWhenEmpty(type) {
        if (this.props[type.type].length < type.displayCount) {
            this.props.fetchFeed()
        }
    }
}