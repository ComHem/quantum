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

    getStyledMessage() {
        const hashTags = /#([\w]+)/gi;
        const atTags = /@([\w]+)/gi;
        let hashReplace = reactStringReplace(this.props.post.message, hashTags, (match) => (
            <span className="hashtag">#{match}</span>
        ));

        return reactStringReplace(hashReplace, atTags, (match) => (
            <span className="hashtag">@{match}</span>
        ));
    }

    getFormattedDate() {
        //return moment(new Date(this.props.post.date)); //TODO JavaFix. Fix in Mapper, need more fix.
        return "DATE";
    }

    render() {
        return (
            <div className={`message--item message--item__${this.type} ${this.props.post.platform}`}>
                <div className={`message--item__single--content`}>
                    <div className="author--icon__container">
                        <div className="author--icon__pull-left">
                            <img className="author-icon" src={this.props.post.authorImg}/>
                            <div className="message--item--platform-icon">
                                {this.props.getPlatformIcon(this.props.post.platform)}
                            </div>
                        </div>
                        <div className="message--item__description">
                            <p className="author-name">{this.props.post.author}</p>
                            <p className="date__pull-right">{this.getFormattedDate()}</p>
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
                        {this.props.post.replies.map((post, j) => <ThreadReplies key={j} post={post}/>)}
                    </div> : null
                }
            </div>
        );
    }
}