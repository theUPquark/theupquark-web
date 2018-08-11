import React from 'react'
import { withStyles } from '@material-ui/core/styles';
import AchievementFrame from '../images/achievementTemplateLong.png'

const styles = theme => ({
  root: {
    position: 'relative',
    width: '512px',
    height: '95px',
  },
  image: {
    position: 'absolute',
    left: '0px',
    top: '0px',
  },
  title: {
    position: 'absolute',
    width: '350px',
    height: '20px',
    textAlign: 'center',
    fontSize: '14px',
    left: '80px',
    top: '10px',
    color: 'white',
  },
  points: {
    position: 'absolute',
    width: '25px',
    textAlign: 'center',
    left: '451px',
    top: '30px',
    color: 'white',
  },
  description: {
    position: 'absolute',
    width: '350px',
    height: '30px',
    textAlign: 'center',
    fontSize: '9px',
    left: '80px',
    top: '35px',
    color: 'black',
  },
  time: {
    position: 'absolute',
    width: '79px',
    left: '424px',
    top: '64px',
    textAlign: 'center',
    fontSize: '10px',
    color: 'gold',
    fontWeight: 'bold',
  },
})
class Achievement extends React.Component {

  convertTimeMilliToDate = (timeMilli) => {
    let date = new Date(parseInt(timeMilli, 10))
    return date.getMonth() + '/' + date.getDate() + '/' + date.getFullYear()
  }

  render() {
    return (
      <div className={this.props.classes.root}>
        <img src={AchievementFrame} alt=""
          className={this.props.classes.image}/>
        <div className={this.props.classes.title}>
          {this.props.title}
        </div>
        <div className={this.props.classes.points}>
          {this.props.points}
        </div>
        <div className={this.props.classes.description}>
          {this.props.desc}
        </div>
        <div className={this.props.classes.time}>
          {this.convertTimeMilliToDate(this.props.time)}
        </div>
      </div>
    )
  }
}

Achievement.defaultProps = {
  points: '10',
  title: 'Crab Battle',
  desc: 'Witness the snake battle the crab. Is it a cave demon? Just remember your CQC. (rip)',
  time: '1270925040000',
  reward: undefined,
  icon: undefined,
}

export default withStyles(styles)(Achievement);
