import React, {PureComponent} from 'react';


export default class ThreadReplies extends PureComponent {
    render() {
        return (
            <div className="thread-replies">
                <div className="author-name thread-author">{this.props.post.author}</div>
                <div className="thread__message">{this.props.children}
                    <p>{this.props.getStyledMessage(this.props.post.message)}</p>
                    {this.props.post.replies && this.props.post.replies.map((post, j) => (
                        <ThreadReplies key={j} getStyledMessage={this.props.getStyledMessage.bind(this)} post={post}>{" "}</ThreadReplies>
                    ))}
                </div>
            </div>
        );
    }
}