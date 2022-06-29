extern crate core;

mod base;
mod play;

use crate::base::db::{Database, DatabaseConfig, MutationStore};
use crate::base::proc::SequenceRowToColumnTransformer;
use crate::base::seq_compression::SeqCompressor;
use crate::base::util::ExecutorService;
use crate::base::ProgramConfig;
use crate::base::{db, RefGenomeConfig};
use chrono::Local;
use config::{Config, File, FileFormat};
use serde::Deserialize;
use std::borrow::Borrow;
use std::path::Path;
use std::sync::{Arc, Mutex};
use std::time::Duration;
use std::{thread, time};

// fn test() {
//     let transformer = SequenceRowToColumnTransformer::new(2, 5);
//     let mut compressor = SeqCompressor::new();
//     let sequences = vec!["ABCDEFGHIJKLMNOP", "abcdefghijklmnop", "0123456789abcdef"];
//     let compressed_sequences: Vec<Option<Vec<u8>>> = sequences
//         .iter()
//         .map(|s| Some(compressor.compress_bytes(s.as_bytes())))
//         .collect();
//     let results = Arc::new(Mutex::new(Vec::<(usize, Vec<Vec<u8>>)>::new()));
//     let results_ref_copy = results.clone();
//     let consume = move |pos_offset, transformed_seqs| {
//         let mut y = results_ref_copy.lock().unwrap();
//         y.push((pos_offset, transformed_seqs));
//     };
//     transformer.transform(
//         &compressed_sequences,
//         &compressor,
//         &compressor,
//         Arc::new(Mutex::new(consume)),
//         b'!',
//     );
//     let mut x = results.lock().unwrap();
//     x.sort_by_key(|x| x.0);
//     let y: Vec<_> = x.iter()
//         .map(|x| &x.1)
//         .flatten()
//         .map(|x| String::from_utf8(compressor.decompress(x)).unwrap())
//         .collect();
//     println!("{:?}", y);
//     println!("Done");
// }

fn main() {
    println!("{} Welcome", Local::now());
    // test();

    let config = read_config();
    let ref_genome_config = read_ref_genome_config();
    let mut nuc_seq_compressor = SeqCompressor::with_dict(ref_genome_config.sequence.as_bytes());

    play::hash_uppercase(&config.database, &mut nuc_seq_compressor);
    todo!("Stop here");

    // db::generate_db_tables(&config.database, &config.schema);
    // base::proc::load_source_data(
    //     Path::new("/Users/chachen/polybox/tmp_mpox/data"),
    //     &config.schema,
    //     &config.database,
    // );
    // base::proc::load_source_data(
    //     Path::new("E:/polybox/tmp_mpox/data"),
    //     &config.schema,
    //     &config.database,
    //     &mut nuc_seq_compressor,
    // );
    base::proc::source_to_main(&config.schema, &config.database, &mut nuc_seq_compressor);

    // let db = Database::load(&config.database);
    //
    // for _ in 0..20 {
    //     let ids = random_ids(db.size, 0.2);
    //     let start = time::Instant::now();
    //     let nucs = db.nuc_muts(&ids);
    //     let duration = start.elapsed();
    //     println!("In {} sequences, I found a total of {} nucleotide mutations ({}ms)", ids.len(), nucs.len(), duration.as_millis());
    //     thread::sleep(time::Duration::from_secs(3));
    // }
}

fn read_config() -> ProgramConfig {
    let builder = Config::builder().add_source(File::new("config.yml", FileFormat::Yaml));
    let config_unparsed = builder.build().expect("The config file cannot be loaded.");
    let config: ProgramConfig = config_unparsed
        .try_deserialize()
        .expect("The config file is invalid.");
    config
}

fn read_ref_genome_config() -> RefGenomeConfig {
    let builder =
        Config::builder().add_source(File::new("config_ref_genome.yml", FileFormat::Yaml));
    let config_unparsed = builder
        .build()
        .expect("The ref genome config file cannot be loaded.");
    let config: RefGenomeConfig = config_unparsed
        .try_deserialize()
        .expect("The ref genome config file is invalid.");
    config
}

fn random_ids(max: u32, prob: f64) -> Vec<u32> {
    let mut ids = Vec::new();
    for i in 0..max {
        let rand_value: f64 = rand::random();
        if rand_value < prob {
            ids.push(i);
        }
    }
    ids
}
