class SubjectList extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        if(!this.props.subjects) {
            return null;
        }
        let buttons = [];
        for(let i = 0; i < this.props.subjects.length && i < 5; i++) {
            let subject = this.props.subjects[i];
            buttons.push(<a-entity onClick={(evt) => VRPedia.instance.loadSubject(subject.Concept)}
                         id={subject.Concept}
                         rotation="0 -30 0"
                         text-button={`width:2; text:${subject.conceptLabel}`}>
                         </a-entity>)
        }

        return (<a-entity layout="type:circle; plane:xz; reverse:1; margin:-0.5; angle:-20; radius:5" rotation="0 30 0">
                {buttons}
                </a-entity>);
    }
}
