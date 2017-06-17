'use strict';

import CommonMixin from '../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, $, reactMixin} = window._external;

@reactMixin.decorate(CommonMixin)
class KindEditor extends Component {

    editor = null;
    oldValue = '';

    bindFns = ['handleChange'];

    constructor(props) {
        super(props);
        this.oldValue = props.value || '';
    }

    init() {
        this.renderKindEditor();
    }

    update() {
        const {value} = this.props;
        const editor = this.editor;
        if (editor) {
            this.setValue(value);
        }
    }

    destroy() {
        this.destroyKindEditor();
    }

    shouldComponentUpdate(newProps) {
        const {value, _refresh} = this.props;
        return value !== newProps.value || _refresh !== newProps._refresh;
    }

    handleChange() {
        const {onChange} = this.props;
        const editor = this.editor;
        if (editor) {
            const newValue = editor.html() || '';
            if (newValue !== this.oldValue) {
                onChange && onChange(editor);
                this.oldValue = newValue;
            }
        }
    }

    destroyKindEditor() {
        const {target} = this.props;
        const editor = this.editor;
        if (editor) {
            const kindEditorEl = $(target).prev('.ke-container');
            kindEditorEl.remove();
            this.editor = null;
        }
    }

    renderKindEditor() {
        const {target, value} = this.props;
        const el = $(target);
        this.destroyKindEditor();
        this.editor = window.KindEditor.create(el[0], {
            resizeType: 1,
            allowImageUpload: false,
            items: [
                'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                'removeformat', 'table', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                'insertunorderedlist', '|', 'image', 'link'
            ],
            afterChange: this.handleChange,
            height: 500
        });
        this.setValue(value);
    }

    setValue(value = '') {
        const editor = this.editor;
        if (editor) {
            editor.html(value);
        }
    }

    render() {
        return null;
    }

}

KindEditor.propTypes = {
    target: PropTypes.any.isRequired,
    onChange: PropTypes.func,
    value: PropTypes.string,
    _refresh: PropTypes.any
};

export {KindEditor};

export default connect(state => ({}))(KindEditor);
