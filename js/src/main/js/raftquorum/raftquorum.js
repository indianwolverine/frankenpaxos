let client_info = {
  props: ['node'],

  data: function () {
    return {
      writeKey: "",
      writeValue: "",
      readKey: "",
    };
  },

  methods: {
    write: function () {
      if (this.writeKey === "" || this.writeValue === "") {
        return;
      }
      console.log(this.node.actor.write(this.raft.serializeWrite(this.writeKey, this.writeValue)));
      this.writeKey = "";
      this.writeValue = "";
    },
    read: function () {
      if (this.readKey === "") {
        return;
      }
      console.log(this.node.actor.read(this.raft.serializeRead(this.readKey)));
      this.readKey = "";
    }
  },

  template: `
      <div>
        <div><strong>Leader Index</strong>: {{node.actor.leaderIndex}} </div>
        <div><strong>Participants</strong>: {{node.actor.raftParticipants}} </div>
        <div><strong>Quorum System</strong>: {{node.actor.quorumSystem}} </div>
        <div><strong>Pending Action</strong>: {{node.actor.pending}} </div>
        <div>
          <button v-on:click="write">Write (Key, Value)</button>
          <input v-model="writeKey"></input>
          <input v-model="writeValue"></input>
        </div>
        <div>
          <button v-on:click="read">Read (Key)</button>
          <input v-model="readKey" v-on:keyup.enter="read"></input>
        </div>
      </div>
    `,
};

let participant_info = {
  props: ['node'],
  template: `
  <div>
    <div><strong>Participants</strong>: {{node.actor.participants}}</div>
    <div><strong>Clients</strong>: {{node.actor.clients}}</div>
    <div><strong>State</strong>: {{node.actor.state}}</div>
    <div><strong>Log</strong>: {{node.actor.log}}</div>
    <div><strong>Commit Index</strong>: {{node.actor.commitIndex}}</div>
    <div><strong>Last Applied</strong>: {{node.actor.lastApplied}}</div>
    <div><strong>State Machine (KV Store)</strong>: {{node.actor.stateMachine}}</div>
    <div style="font-style: italic">
      <div><strong>Next Index</strong>: {{node.actor.nextIndex}}</div>
      <div><strong>Match Index</strong>: {{node.actor.matchIndex}}</div>
      <div><strong>Client Write Return</strong>: {{node.actor.clientWriteReturn}}</div>
      <div><strong>Client Read Return</strong>: {{node.actor.clientReads}}</div>
    </div>
  </div>
  `,
};

// Returns numbers drawn from an exponential distribution with mean `mean`.
let exponential = function (mean) {
  return -Math.log(Math.random() + Number.EPSILON) * mean
}

let raft_state_colors = {
  leaderless_follower: '#f1c40f',
  follower: '#27ae60',
  candidate: '#3498db',
  leader: '#e74c3c',
}

function make_nodes(RaftQuorum, snap) {
  // https://flatuicolors.com/palette/defo
  let flat_red = '#e74c3c';
  let flat_blue = '#3498db';
  let flat_green = '#2ecc71';
  let colored = (color) => {
    return {
      'fill': color,
      'stroke': 'black', 'stroke-width': '3pt',
    }
  };

  let number_style = {
    'text-anchor': 'middle',
    'alignment-baseline': 'middle',
    'font-size': '20pt',
    'font-weight': 'bolder',
    'fill': 'black',
    'stroke': 'white',
    'stroke-width': '1px',
  }

  let polar_to_cartesian = function (x_origin, y_origin, theta, r) {
    let dx = Math.cos(theta) * r;
    let dy = Math.sin(theta) * r;
    return [x_origin + dx, y_origin - dy];
  }

  let nodes = {};
  let title_attr = { 'text-anchor': 'middle', 'alignment-baseline': 'middle' };
  let x_origin = 200;
  let y_origin = 150;
  let theta = 2 * Math.PI / 5;
  let r = 100;

  // Node positions.
  let [ax, ay] = polar_to_cartesian(x_origin, y_origin, 1 * theta, r);
  let [bx, by] = polar_to_cartesian(x_origin, y_origin, 2 * theta, r);
  let [cx, cy] = polar_to_cartesian(x_origin, y_origin, 3 * theta, r);
  let [dx, dy] = polar_to_cartesian(x_origin, y_origin, 4 * theta, r);
  let [ex, ey] = polar_to_cartesian(x_origin, y_origin, 5 * theta, r);

  // Title positions.
  let [tax, tay] = polar_to_cartesian(x_origin, y_origin, 1 * theta, r + 40);
  let [tbx, tby] = polar_to_cartesian(x_origin, y_origin, 2 * theta, r + 40);
  let [tcx, tcy] = polar_to_cartesian(x_origin, y_origin, 3 * theta, r + 40);
  let [tdx, tdy] = polar_to_cartesian(x_origin, y_origin, 4 * theta, r + 40);
  let [tex, tey] = polar_to_cartesian(x_origin, y_origin, 5 * theta, r + 40);

  nodes[RaftQuorum.participant1.address] = {
    actor: RaftQuorum.participant1,
    svgs: [
      snap.circle(ax, ay, 20).attr(colored(raft_state_colors.leaderless_follower)),
      snap.text(ax, ay, '0').attr(title_attr),
      snap.text(tax, tay, 'a').attr(title_attr),
    ],
    component: participant_info,
  };
  nodes[RaftQuorum.participant2.address] = {
    actor: RaftQuorum.participant2,
    svgs: [
      snap.circle(bx, by, 20).attr(colored(raft_state_colors.leaderless_follower)),
      snap.text(bx, by, '0').attr(title_attr),
      snap.text(tbx, tby, 'b').attr(title_attr),
    ],
    component: participant_info,
  };
  nodes[RaftQuorum.participant3.address] = {
    actor: RaftQuorum.participant3,
    svgs: [
      snap.circle(cx, cy, 20).attr(colored(raft_state_colors.leaderless_follower)),
      snap.text(cx, cy, '0').attr(title_attr),
      snap.text(tcx, tcy, 'c').attr(title_attr),
    ],
    component: participant_info,
  };
  nodes[RaftQuorum.participant4.address] = {
    actor: RaftQuorum.participant4,
    svgs: [
      snap.circle(dx, dy, 20).attr(colored(raft_state_colors.leaderless_follower)),
      snap.text(dx, dy, '0').attr(title_attr),
      snap.text(tdx, tdy, 'd').attr(title_attr),
    ],
    component: participant_info,
  };
  nodes[RaftQuorum.participant5.address] = {
    actor: RaftQuorum.participant5,
    svgs: [
      snap.circle(ex, ey, 20).attr(colored(raft_state_colors.leaderless_follower)),
      snap.text(ex, ey, '0').attr(title_attr),
      snap.text(tex, tey, 'e').attr(title_attr),
    ],
    component: participant_info,
  };

  // Clients.
  nodes[RaftQuorum.client1.address] = {
    actor: RaftQuorum.client1,
    svgs: [
      snap.circle(0, 50, 20).attr(colored(flat_red)),
      snap.text(0, 52, '1').attr(number_style),
    ],
    color: flat_red,
    component: client_info,
  }
  nodes[RaftQuorum.client2.address] = {
    actor: RaftQuorum.client2,
    svgs: [
      snap.circle(0, 150, 20).attr(colored(flat_red)),
      snap.text(0, 152, '2').attr(number_style),
    ],
    color: flat_red,
    component: client_info,
  }
  nodes[RaftQuorum.client3.address] = {
    actor: RaftQuorum.client3,
    svgs: [
      snap.circle(0, 250, 20).attr(colored(flat_red)),
      snap.text(0, 252, '3').attr(number_style),
    ],
    color: flat_red,
    component: client_info,
  }

  return nodes
}

function main() {
  let RaftQuorum =
    frankenpaxos.raftquorum.TweenedRaftQuorum.RaftQuorum;
  let snap = Snap('#animation');
  let nodes = make_nodes(RaftQuorum, snap);

  let state_to_color = function (state) {
    // scala.js does not let you nicely pattern match on an ADT. Thus, we do
    // something hacky and inspect the name of the constructor.
    let name = state.constructor.name;
    if (name.includes('Participant$LeaderlessFollower')) {
      return raft_state_colors.leaderless_follower;
    } else if (name.includes('Participant$Follower')) {
      return raft_state_colors.follower;
    } else if (name.includes('Participant$Candidate')) {
      return raft_state_colors.candidate;
    } else if (name.includes('Participant$Leader')) {
      return raft_state_colors.leader;
    }
  };

  let node_watch = {
    deep: true,
    handler: function (node) {
      node.svgs[0].attr({ fill: state_to_color(node.actor.state) });
      node.svgs[1].attr({ text: node.actor.term });
    },
  }

  // Create the vue app.
  let vue_app = new Vue({
    el: '#app',

    data: {
      nodes: nodes,
      node: nodes[RaftQuorum.participant1.address],
      transport: RaftQuorum.transport,
      settings: {
        time_scale: 1,
        auto_deliver_messages: true,
        auto_start_timers: true,
      },
      a: nodes[RaftQuorum.participant1.address],
      b: nodes[RaftQuorum.participant2.address],
      c: nodes[RaftQuorum.participant3.address],
      d: nodes[RaftQuorum.participant4.address],
      e: nodes[RaftQuorum.participant5.address],
    },

    methods: {
      send_message: function (message) {
        let src = this.nodes[message.src];
        let dst = this.nodes[message.dst];
        let src_x = src.svgs[0].attr("cx");
        let src_y = src.svgs[0].attr("cy");
        let dst_x = dst.svgs[0].attr("cx");
        let dst_y = dst.svgs[0].attr("cy");

        let svg_message = snap.circle(src_x, src_y, 9).attr({ fill: '#2c3e50' });
        snap.prepend(svg_message);
        let duration = (500 + exponential(500)) / 1000;
        return TweenMax.to(svg_message.node, duration, {
          attr: { cx: dst_x, cy: dst_y },
          ease: Linear.easeNone,
          onComplete: () => { svg_message.remove(); },
        });
      },

      partition: function (address) {
        this.nodes[address].svgs[2].attr({ fill: "#7f8c8d" });
      },

      unpartition: function (address) {
        this.nodes[address].svgs[2].attr({ fill: "black" });
      },
    },

    watch: {
      a: node_watch,
      b: node_watch,
      c: node_watch,
      d: node_watch,
      e: node_watch,
    },
  });

  Vue.mixin({
    data() {
      return {
        raft: RaftQuorum,
      }
    }
  })

  // Select a node by clicking it.
  for (let node of Object.values(nodes)) {
    for (let svg of node.svgs) {
      svg.node.onclick = () => {
        vue_app.node = node;
      }
    }
  }
}

window.onload = main
