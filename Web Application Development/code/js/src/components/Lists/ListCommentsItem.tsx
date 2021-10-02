import React from 'react'
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Typography from '@material-ui/core/Typography';
import { createStyles, Theme, makeStyles } from '@material-ui/core/styles';

import * as Siren from '../../Siren'
import { commentsDto, commentDto } from '../../models'


export function ListCommentsItem({ entities }: Siren.Entity<commentsDto, commentDto>) {

    function convertDate(date: Date) {
        return date.toLocaleDateString() + " " + date.toLocaleTimeString();
    }

    const classes = useStyles();
    return (
        <List className={classes.root}>
            {entities.map((ent) => 
                <ListItem alignItems="flex-start" key={ent.properties.id}>
                    <ListItemText
                    primary={`ID: ${ent.properties.id}`}
                    secondary={
                        <React.Fragment>
                            <Typography
                                component="span"
                                variant="body2"
                                className={classes.inline}
                                color="textPrimary"
                            >
                                {convertDate(new Date(ent.properties.date))}
                            </Typography>
                            {" — " + ent.properties.text}
                        </React.Fragment>
                    }
                    />
                </ListItem>
            )}
        </List>
    )
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      width: '100%',
      maxWidth: '600px',
      backgroundColor: theme.palette.background.paper,
    },
    inline: {
      display: 'inline',
    },
  }),
);
