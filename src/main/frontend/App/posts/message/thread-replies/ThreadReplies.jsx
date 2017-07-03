import React, {PureComponent} from 'react';


export default class ThreadReplies extends PureComponent {
    render() {
        return (
            <div className="thread-replies">
                <div className="thread-author">{this.props.post.author}</div>
                <div className="thread__message">
                    <i className="reply--icon">{this.props.children}</i>
                    <p>{this.props.post.message}</p>
                </div>
                {this.props.post.replies && this.props.post.replies.map((post, j) => (
                    <ThreadReplies key={j} post={post}>{"->"}</ThreadReplies>
                ))}
            </div>
        );
    }
}