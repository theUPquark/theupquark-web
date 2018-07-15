import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { withStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import Input from '@material-ui/core/Input';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import Achievement from './components/Achievement'

const styles = theme => ({
  form: {
    color: 'green',
  },
  character: {
    margin: theme.spacing.unit,
  },
  button: {
    margin: theme.spacing.unit,
  }
})

class App extends Component {
  state = {
    region: 'us',
    characters: [
      {
        name: 'rhetiaya',
        server: 'argent-dawn',
      },
      {
        name: 'errai',
        server: 'argent-dawn',
      },
    ]
  }
  handleChange = name => event => {
    this.setState({
      [name]: event.target.value,
    })
  }

  handleCharacter = (field, index) => event => {
    let change = this.state.characters
    change[index][field] = event.target.value

    this.setState({
      characters: change
    })
  }

  characterInput = (id) => {
    return (
      <div key={`char-${id}`}>
        <Button mini variant="fab" color="secondary" aria-label="Add"
          className={this.props.classes.character}
          onClick={() => {this.deleteCharacter(id)}}>-</Button>
        <TextField
          label="name"
          key={`char-${id}`}
          id={`character-${id}`}
          name={`characters[${id}]`}
          className={this.props.classes.character}
          value={this.state.characters[id].name}
          onChange={this.handleCharacter('name', id)}
        />
        <TextField
          label="server"
          key={`server-${id}`}
          id={`server-${id}`}
          className={this.props.classes.character}
          value={this.state.characters[id].server}
          onChange={this.handleCharacter('server', id)}
        />
      </div>
    )
  }

  addCharacter = () => {
    console.log('add a thing')
    let change = [...this.state.characters]
    change.push({name: '', server: ''})

    this.setState({
      characters: change
    })
  }

  deleteCharacter = (index) => {
    let change = [...this.state.characters]
    
    if (change.length > 2) {
      change.splice(index, 1)

      this.setState({
        characters: change
      })
    }
  }

  compareAchievementsRequest = () => {
    console.log('send request')
  }

  render() {
    let chars = []
    for (let i = 0; i < this.state.characters.length; i++) {
      chars.push(this.characterInput(i))
    }
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
      <form className={this.props.classes.form}>
        <Select
          name="region"
          label="region"
          inputProps={{name: 'region', id:'region'}}
          value={this.state.region}
          onChange={this.handleChange('region')}
        >
          <MenuItem value={'us'}>US</MenuItem>
          <MenuItem value={'eu'}>EU</MenuItem>
          <MenuItem value={'kr'}>KR</MenuItem>
          <MenuItem value={'tw'}>TW</MenuItem>
        </Select>
        {chars}
        <Button mini variant="fab" color="primary" aria-label="Add"
          onClick={this.addCharacter}>
        +
        </Button>
        <Button size="small" color="primary" aria-label="Add"
          onClick={this.compareAchievementsRequest}>
        Compare Achievements
        </Button>
      </form>
      <GridList>
        <Achievement/>
        <Achievement/>
        <Achievement/>
      </GridList>
      </div>
    );
  }
}

export default withStyles(styles)(App);
