import React, {PureComponent} from 'react';
import ThreadReplies from './thread-replies/ThreadReplies';
import reactStringReplace from 'react-string-replace';
import moment from 'moment';
import moment_se from 'moment/locale/sv';
import CountTo from './counter/CountTo';


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
                    <div className="author--icon__container">

                        <div className="author--icon__pull-left">
                            <img className="author-icon" src={this.props.post.authorImg}/>

                            <div className="message--item--platform-icon">
                                {this.props.getPlatformIcon(this.props.post.platform)}
                            </div>

                        </div>
                        <div className="message--item__description">
                            <p className="author-name">{this.props.post.author}</p>
                            <p className="date">{this.getFormattedDate()}</p>
                        </div>
                        {this.props.post.reactions && this.getReactions()}
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

    getReactionIcon(reactionType) {
        return (<i className={`fa fa-${reactionType.toString().toLowerCase()}`}/>);

    }

    getReactions() {

        return (this.props.post.reactions.map((reaction, i) => {

                return <span className="reactions" key={i}>
                    <span>
                        {this.getReactionIcon(Object.keys(reaction))}
                       </span>
                    <CountTo to={Object.values(reaction)} speed={3000}>
                    </CountTo>
                </span>


            }
        ))
    }
}