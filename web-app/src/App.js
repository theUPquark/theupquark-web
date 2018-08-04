import React, { Component } from 'react';
import axios from 'axios'
import './App.css';
import { withStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import GridList from '@material-ui/core/GridList';
//import GridListTile from '@material-ui/core/GridListTile';
import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
//import Input from '@material-ui/core/Input';
import Button from '@material-ui/core/Button';
//import IconButton from '@material-ui/core/IconButton';
//import Icon from '@material-ui/core/Icon';
import Paper from '@material-ui/core/Paper'
import Achievement from './components/Achievement'

const styles = theme => ({
  form: {
    display: 'flex',
    justifyContent: 'center',
  },
  character: {
    margin: theme.spacing.unit,
  },
  button: {
    margin: theme.spacing.unit,
    fontSize: '29px',
  }
})

class App extends Component {
  state = {
    region: 'us',
    characters: [
      {
        name: 'rhetaiya',
        server: 'argent-dawn',
      },
      {
        name: 'errai',
        server: 'argent-dawn',
      },
    ],
    achievements: [
      {
        title: 'Crab Battle',
        points: '10',
        description: 'Witness the snake battle the crab. Is it a cave demon? Just remember your CQC. (rip)',
        time: '2018',
      },
      {
        title: 'Crab Battle',
        points: '10',
        description: 'Witness the snake battle the crab. Is it a cave demon? Just remember your CQC. (rip)',
        time: '2018',
      },
    ],
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
      <Grid item xs={12} key={`char-${id}`}>
        <Button mini variant="fab" color="secondary" aria-label="Add"
          className={this.props.classes.button}
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
      </Grid>
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
    axios({
      method: 'POST',
      url: 'http://localhost:8080/compare',
      data: {
        characters: this.state.characters,
        region: this.state.region,
      },
    }).then((response) => {
      console.log('success')
      this.setState({
        achievements: response.data
      })
    }).catch((error) => {
      console.log(error)
    })
  }

  pushAchievement = (idx, achieve) => {
    return (
      <Achievement key={idx}
        title={achieve.title}
        points={achieve.points}
        desc={achieve.description}
      />
    )
  }
  render() {
    let chars = []
    for (let i = 0; i < this.state.characters.length; i++) {
      chars.push(this.characterInput(i))
    }

    let achives = []
    for (let i = 0; i < this.state.achievements.length; i++) {
      achives.push(this.pushAchievement(i, this.state.achievements[i]))
    }
    return (
      <div className="App">
        <Grid container className={this.props.classes.form}>
          <Paper>
            <Grid item xs={12}>
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
            </Grid>
            {chars}
            <Grid item xs={12}>
              <Button mini variant="fab" color="primary" aria-label="Add"
                className={this.props.classes.button}
                onClick={this.addCharacter}>
              +
              </Button>
            </Grid>
            <Grid item xs={12}>
              <Button variant="outlined" size="small" color="primary"
                aria-label="Add"
                onClick={this.compareAchievementsRequest}>
              Compare Achievements
              </Button>
            </Grid>
          </Paper>
        </Grid>
        <hr/>
        <GridList>
          {achives}
        </GridList>
      </div>
    );
  }
}

export default withStyles(styles)(App);
