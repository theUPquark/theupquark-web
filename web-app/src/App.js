import React, { Component } from 'react';
import axios from 'axios'
import './App.css';
import { withStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl'
import Grid from '@material-ui/core/Grid';
import GridList from '@material-ui/core/GridList';
//import GridListTile from '@material-ui/core/GridListTile';
import InputLabel from '@material-ui/core/InputLabel'
import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import Typography from '@material-ui/core/Typography'
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
  achievementList: {
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
    loading: false,
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
        time: '1281060000000',
      },
      {
        title: 'Crab Battle',
        points: '10',
        description: 'Witness the snake battle the crab. Is it a cave demon? Just remember your CQC. (rip)',
        time: '1270925040000',
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
    this.setState({
      loading: true
    })
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
        loading: false,
        achievements: response.data
      })
    }).catch((error) => {
      this.setState({
        loading: false
      })
      console.log(error)
    })
  }

  pushAchievement = (idx, achieve) => {
    return (
      <Achievement key={idx}
        title={achieve.title}
        points={achieve.points}
        desc={achieve.description}
        time={achieve.time}
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
          <Typography variant="display1">
            Warcraft Achievement Comparator
          </Typography>
          <Typography variant="body2"
            style={{
              width: '100%',
              paddingBottom: '15px'
            }}
          >
            Designate multiple characters to query, and view a selection of
            achievements that all characters completed together.
          </Typography>
          <Paper>
            <Grid item xs={12}>
              <FormControl>
                <InputLabel shrink htmlFor="achive-form-region">
                  Region
                </InputLabel>
                <Select
                  id="achive-form-region"
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
              </FormControl>
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
                disabled={this.state.loading}
                onClick={this.compareAchievementsRequest}>
              Compare Achievements
              </Button>
            </Grid>
          </Paper>
        </Grid>
        <hr/>
        <GridList className={this.props.classes.achievementList}>
          {achives}
        </GridList>
      </div>
    );
  }
}

export default withStyles(styles)(App);
