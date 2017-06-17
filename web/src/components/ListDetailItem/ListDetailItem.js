'use strict';

const {React, Component, PropTypes, connect} = window._external;

class ListDetailItem extends Component {

    render() {
        const {type, label, content} = this.props;
        return (
            type === 'textarea' || type === 'image' || type === 'richtext' ?
                <div className="am-list-item am-list-item-middle">
                    <div className="am-list-line">
                        <div className="am-list-content">
                            <div className="x-label">
                                {label}
                            </div>
                            <div>
                                {type === 'textarea' ? <pre className="x-pre">{content}</pre> : ''}
                                {type === 'image' ? content : ''}
                                {type === 'richtext' ? <div dangerouslySetInnerHTML={{__html: content}}></div> : ''}
                            </div>
                        </div>
                    </div>
                </div> :
                <div className="am-list-item am-textarea-item">
                    <div className="am-textarea-label am-textarea-label-7">
                        {label}
                    </div>
                    <div className="am-textarea-control x-detail-item">
                        {content}
                    </div>
                </div>
        );
    }

}

ListDetailItem.propTypes = {
    type: PropTypes.string,
    label: PropTypes.string,
    content: PropTypes.any
};

export {ListDetailItem};

export default connect(state => ({}))(ListDetailItem);
