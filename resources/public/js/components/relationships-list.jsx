class RelationshipsList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selected:null,
        };
    }

    selectSubject(rel) {
        this.setState({selected: rel});
    }

    render() {
        let buttons = [];
        for(let key in this.props.references) {
            buttons.push(<a-entity
                         onClick={(evt) => this.selectSubject(key)}
                         id={key}
                         rotation="0 -30 0"
                         text-button={`width:2; text:${key}`}> </a-entity>)
        }

        return (<a-entity>
                <a-entity layout="type:circle; plane:xz; reverse:0; margin:1.1; radius:5; angle:-20" rotation="0 30 0">
                    {buttons}
                </a-entity>
                <a-entity position="0 -0.5 0" rotation="0 0 0">
                    {this.state.selected ? <SubjectList subjects={this.props.references[this.state.selected]}/> : null}
                </a-entity>
               </a-entity>);
    }
}
