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
                        <ThreadReplies key={i}
                                       getStyledMessage={this.props.getStyledMessage.bind(this)}
                                       style={{
                                           filter: `rotate-hue(${(i + 1) * 20}deg)`
                                       }}
                                       post={post}/>
                    ))}
                </div>
            </div>
        );
    }
}