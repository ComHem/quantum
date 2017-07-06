import React, {PureComponent} from 'react';
import moment from 'moment';

export default class ThreadReplies extends PureComponent {
    getFormattedDate() {
        moment.locale('sv');
        return moment(new Date(this.props.post.date)).calendar();
    }

    render() {
        return (
            <div className="thread-replies">
                <div className="author-name thread-author">{this.props.post.author}</div>
                <div className="message--item__description">
                    <span className="date">{this.getFormattedDate()}</span>
                </div>
                <div className="thread__message">{this.props.children}
                    <p>{this.props.getStyledMessage(this.props.post.message)}</p>
                    {this.props.post.replies && this.props.post.replies.map((post, i) => (
                        <div>
                            <div className="comment-indicator">
                                <i className="fa fa-long-arrow-down"/>
                            </div>
                            <ThreadReplies
                                key={i}
                                getStyledMessage={this.props.getStyledMessage.bind(this)}
                                post={post}/>
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}