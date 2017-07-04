import React, {PureComponent} from 'react';
import ThreadReplies from './thread-replies/ThreadReplies';
import reactStringReplace from 'react-string-replace';
import moment from 'moment';
import moment_se from 'moment/locale/sv';


export default class Message extends PureComponent {
    constructor(props) {
        super(props);

        moment.locale('sv');
        this.type = this.props.type === "singles" ? "single" : "thread";
    }

    getStyledMessage(message = this.props.post.message) {
        const hashTags = /#([\w]+)/gi;
        const atTags = /@([\w]+)/gi;
        let hashReplace = reactStringReplace(message, hashTags, (match) => (
            <span className="hashtag">#{match}</span>
        ));

        return reactStringReplace(hashReplace, atTags, (match) => (
            <span className="hashtag">@{match}</span>
        ));
    }

    getFormattedDate() {
        return moment(new Date(this.props.post.date)).calendar();
    }

    render() {
        return (
            <div className={`message--item message--item__${this.type} ${this.props.post.platform}`}>
                <div className={`message--item__single--content`}>
                    <div className="message--item--platform-icon">
                        {this.props.getPlatformIcon(this.props.post.platform)}
                    </div>
                    <div className="author--icon__container">
                        <div className="author--icon__pull-left">
                            <img className="author-icon" src={this.props.post.authorImg}/>

                        </div>
                        <div className="message--item__description">
                            <p className="author-name">{this.props.post.author}</p>
                            <p className="date">{this.getFormattedDate()}</p>
                        </div>
                    </div>
                    <div className={`message--item--body__single`}>
                        <div className="message--body--wrapper">
                            <p className="message--body--text">
                                {this.getStyledMessage()}
                            </p>
                        </div>
                    </div>
                    <img src={this.props.post.contentLink}/>
                </div>

                {this.props.post.replies && this.props.post.replies.length ?
                    <div className="message-thread__children--container">
                        <div className="message--item--comment-icon">
                            <i className="fa fa-commenting"/>
                        </div>
                        {this.props.post.replies.map((post, j) => <ThreadReplies key={j}
                                                                                 getStyledMessage={this.getStyledMessage.bind(this)}
                                                                                 post={post}/>)}
                    </div> : null
                }
            </div>
        );
    }
}