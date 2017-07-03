import React, {PureComponent} from 'react';


export default class ThreadReplies extends PureComponent {
    render() {
        return (
            <div className="thread-replies">
                <div className="thread__message">
                    <i className="reply--icon"> {this.props.children} </i>{this.props.post.message}
                </div>
                {this.props.post.replies && this.props.post.replies.map((post, j) => (
                    <ThreadReplies key={j} post={post}>{"->"}</ThreadReplies>
                ))}
            </div>
        );
    }
}