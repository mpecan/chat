import React, { Component, PropTypes } from 'react';


export default class App extends Component {
  static propTypes = {
    children: PropTypes.object,
  };


  render() {
    const { children } = this.props;

    return (
      <div className='App group'>

        <div className='Page'>
          { children }
        </div>
      </div>
    );
  }
}
