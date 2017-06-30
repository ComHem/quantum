import React, {PureComponent} from 'react';
import ThreadReplies from './thread-replies/ThreadReplies';

export default class Message extends PureComponent {
    constructor(props) {
        super(props);

        this.type = this.props.type === "singles" ? "single" : "thread";
    }

    render() {
        return (
            <div className={`message--item message--item__${this.type} ${this.props.post.plattform.toLowerCase()}`}>
                <div className={`message--item__single--content`}>
                    <div className="author--icon__container">
                        <div className="author--icon__pull-left">
                            <img className="author-icon" src={this.props.post.authorImg}/>
                            <div className="message--item--platform-icon">{this.props.getPlatformIcon(this.props.post.plattform)}</div>
                        </div>
                        <div className="message--item__description">
                            <p className="author-title">{this.props.post.author}</p>
                            <p className="date__pull-right">{this.props.post.date}</p>
                        </div>
                    </div>
                    <div className={`message--item--body__single`}>
                        <div className="message--body--wrapper">
                            <p className="message--body--text">
                                {this.props.post.message}
                            </p>
                        </div>
                    </div>
                    <img src={this.props.post.contentLink}/>
                </div>

                {this.props.post.replies &&
                <div className="message-thread__children--container">{this.props.post.replies.map((post, j) => {
                    return (
                        <ThreadReplies key={j} post={post}/>
                    );
                })}</div>
                }

            </div>
        );
    }
}